package airtouch.v4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.handler.MessageHandler;
import airtouch.v4.internal.MessageHolder;
import airtouch.v4.utils.ByteUtil;
import airtouch.v4.utils.SizedStack;

public class AirtouchConnector implements Runnable{
    
    
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    
    private final ResponseCallback responseCallback;
    private boolean stopping = false;
    
    
    public AirtouchConnector(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public AirtouchConnector connect(String hostName, int portNumber) throws IOException {
        
        // TODO: log connect
        this.socket = new Socket(hostName, portNumber);
        this.input = new BufferedInputStream(socket.getInputStream());
        this.output = new BufferedOutputStream(socket.getOutputStream());
        return this;
    }
    
    public void sendRequest(Request request) throws IOException {
        this.output.write(request.getRequestMessage());
        this.output.flush();
    }

    @SuppressWarnings("rawtypes")
    public void connect() throws IOException {

            int character;

            SizedStack<Byte> bytes = new SizedStack<>(8);
            
            MessageHandler messageHandler = new MessageHandler();
            MessageHolder messageHolder = MessageHolder.initialiseEmpty();
            
            while (!stopping && !socket.isInputShutdown() && (character = input.read()) != -1) {
                // Push the character into the byte stack.
                // The byte stack just stores the last 8 bytes.
                // We use it to determine if we have started a new message.
                bytes.push((byte)character);
                
                // Using the byte stack, check if the last 8 bytes combined
                // look like the beginning of a new message. If they do
                // reset our message data with the 8 bytes.
                // We know now how big our message will be, because bytes 7 & 8
                // specify the data length. Our entire message will be the first
                // 8 bytes, plus the next dataLength bytes, and then 2 more bytes
                // for the check sum. Therefore, we can initialise a buffer with that
                // size.
                if (messageStarted(bytes)) {
                    messageHolder = MessageHolder.initialiseWithData(
                            bytes,
                            ByteUtil.toInt(bytes.get(6), bytes.get(7))
                        );
                } else {
                    // Otherwise, just store our byte and increment the counter.
                    messageHolder.putByte((byte)character);
                }
                // TODO: Use logger
                //System.out.println(java.time.LocalDateTime.now() + " :: " + Integer.toHexString(character));
                
                // If we have reached our total message size, call the handler to parse
                // the message and then re-initialise the buffer to handle the next
                // set of bytes received.
                if(messageHolder.isFinished()) {
                    Response response = messageHandler.handle(messageHolder.getBytes());
                    messageHolder = MessageHolder.initialiseEmpty();
                    responseCallback.handleResponse(response);
                }
            }

            //System.out.println(data);


    }

    private boolean messageStarted(SizedStack<Byte> bytes) {
        if (bytes.size() < 8) return false;
        if (ByteUtil.toInt(bytes.get(0), bytes.get(1)) != MessageConstants.HEADER) return false;
        Address address = Address.getFromBytes(ByteUtil.toInt(bytes.get(2), bytes.get(3)));
        return ByteUtil.toInt(bytes.get(0), bytes.get(1)) == MessageConstants.HEADER
                && (address.equals(Address.STANDARD_RECEIVE) || address.equals(Address.EXTENDED_RECEIVE));
    }

    public void writeMessage(DataOutputStream dout, byte[] msg, int msgLen) throws IOException {
        dout.writeInt(msgLen);
        dout.write(msg, 0, msgLen);
        dout.flush();
    }

    public byte[] readMessage(DataInputStream din) throws IOException {
        int msgLen = din.readInt();
        byte[] msg = new byte[msgLen];
        din.readFully(msg);
        return msg;
    }
    


    public void disconnect() throws IOException {
        //System.out.println("Disconnect called");
        // TODO: Use logger
        this.stopping = true;
        if (this.output != null) {
            this.output.close();
        }
        if (this.input != null) {
            this.input.close();
        }
        if (this.socket != null) {
            this.socket.close();
        }
    }

    @Override
    public void run() {
        try {
            this.connect();
        } catch (SocketException e) {
            // we are expecting a socket exception because we have closed it.
        } catch (IOException e) {
            // Otherwise, log exception.
            e.printStackTrace();
        }
    }

}
