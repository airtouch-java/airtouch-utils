package airtouch.v4;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.handler.MessageHandler;
import airtouch.v4.internal.MessageHolder;
import airtouch.v4.utils.ByteUtil;
import airtouch.v4.utils.SizedStack;

public class AirtouchConnectorThread extends Thread implements Runnable {
    
    private static final String DEFAULT_THREAD_NAME = AirtouchConnectorThread.class.getSimpleName();

    private final Logger log = LoggerFactory.getLogger(AirtouchConnectorThread.class);

    private boolean stopping;
    private final InputStream input;
    private final ResponseCallback responseCallback;


    public AirtouchConnectorThread(final InputStream input, final ResponseCallback responseCallback) {
        super(DEFAULT_THREAD_NAME);
        this.input = input;
        this.responseCallback = responseCallback;
    }
    
    public AirtouchConnectorThread(final InputStream input, final ResponseCallback responseCallback, String threadName) {
        super(threadName);
        this.input = input;
        this.responseCallback = responseCallback;
    }
    
    public void shutdown() {
        this.stopping = true;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        
        int character;

        SizedStack<Byte> bytes = new SizedStack<>(8);
        
        MessageHandler messageHandler = new MessageHandler();
        MessageHolder messageHolder = MessageHolder.initialiseEmpty();
        
        try {
            while (!stopping &&  (character = input.read()) != -1) {
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
                if (log.isTraceEnabled()) {
                    log.trace("Received byte from Airtouch: '{}'", Integer.toHexString(character));
                }
                
                // If we have reached our total message size, call the handler to parse
                // the message and then re-initialise the buffer to handle the next
                // set of bytes received.
                if(messageHolder.isFinished()) {
                    Response response = messageHandler.handle(messageHolder.getBytes());
                    messageHolder = MessageHolder.initialiseEmpty();
                    if (log.isDebugEnabled()) {
                        log.debug("Received response: '{}'", response);
                    }
                    responseCallback.handleResponse(response);
                }
            }
        } catch (SocketException e) {
            // we are expecting a socket exception because we have closed it.
        } catch (IOException e) {
            throw new AirtouchMessagingException("IOException during Airtouch response reading.", e);
        }
    }
    
    private boolean messageStarted(SizedStack<Byte> bytes) {
        if (bytes.size() < 8) return false;
        if (ByteUtil.toInt(bytes.get(0), bytes.get(1)) != MessageConstants.HEADER) return false;
        Address address = Address.getFromBytes(ByteUtil.toInt(bytes.get(2), bytes.get(3)));
        return ByteUtil.toInt(bytes.get(0), bytes.get(1)) == MessageConstants.HEADER
                && (address.equals(Address.STANDARD_RECEIVE) || address.equals(Address.EXTENDED_RECEIVE));
    }

}
