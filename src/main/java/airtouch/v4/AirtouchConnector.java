package airtouch.v4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirtouchConnector {
    
    private final Logger log = LoggerFactory.getLogger(AirtouchConnector.class);
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    
    private final ResponseCallback responseCallback;
    private final String hostName;
    private final int portNumber;
    private AirtouchConnectorThread thread;
    
    
    public AirtouchConnector(final String hostName, final int portNumber, final ResponseCallback responseCallback) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.responseCallback = responseCallback;
    }

    public void start() {
        try {
            this.socket = new Socket(this.hostName, this.portNumber);
            log.info("Connected to Airtouch at '{}:{}'", this.hostName, this.portNumber);
            this.input = new BufferedInputStream(socket.getInputStream());
            this.output = new BufferedOutputStream(socket.getOutputStream());
            this.thread = new AirtouchConnectorThread(input, responseCallback);
            this.thread.start();
        } catch (IOException e) {
            throw new AirtouchMessagingException("Failed to connect to Airtouch", e);
        }
    }
    
    public void sendRequest(Request request) throws IOException {
        if (this.socket == null || this.output == null) {
            throw new AirtouchMessagingException("Failed to send request. Connection not available. Did you call 'start()' first?");
        }
        this.output.write(request.getRequestMessage());
        this.output.flush();
        log.debug("Request sent: '{}'", request.getHexString());
    }

    public void shutdown() {
        log.debug("Shutdown called");
        this.thread.shutdown();
        try {
            if (this.output != null) {
                this.output.close();
            }
            if (this.input != null) {
                this.input.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            throw new AirtouchMessagingException("Exception during AirtouchConnector shutdown." , e);
        }
        log.info("AirtouchConnector shutdown completed");
    }

}
