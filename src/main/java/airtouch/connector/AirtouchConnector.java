package airtouch.connector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.Request;
import airtouch.ResponseCallback;
import airtouch.exception.AirtouchMessagingException;

public class AirtouchConnector<T> {

    private final Logger log = LoggerFactory.getLogger(AirtouchConnector.class);
    private Socket socket;
    private InputStream input;
    private OutputStream output;

    private final ResponseCallback responseCallback;
    private final String hostName;
    private final int portNumber;
    private AirtouchConnectorThread<T> thread;
    private AirtouchConnectorThreadFactory<T> threadFactory;

    public AirtouchConnector(final AirtouchConnectorThreadFactory<T> threadFactory, final String hostName, final int portNumber, final ResponseCallback responseCallback) {
        if (hostName == null || hostName.trim().equals("")) {
            throw new AirtouchMessagingException("hostName is blank. Please pass in a valid hostName when creating an AirtouchConnector instance.");
        }
        if (portNumber < 1) {
            throw new AirtouchMessagingException("portNumber not defined. Please pass in a valid postNumber. The default port for AirTouch4 is 9004. Perhaps try that portNumber when creating an AirtouchConnector instance.");
        }
        if (responseCallback == null) {
            throw new AirtouchMessagingException("responseCallback is null. Please pass in a ResponseCallback instance when creating an AirtouchConnector instance.");
        }
        this.threadFactory = threadFactory;
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
            this.thread = this.threadFactory.create(input, responseCallback);
            this.thread.start();
        } catch (IOException e) {
            throw new AirtouchMessagingException("Failed to connect to Airtouch", e);
        }
    }

    public synchronized void sendRequest(Request<T> request) throws IOException {
        if (this.socket == null || this.output == null) {
            throw new AirtouchMessagingException("Failed to send request. Connection not available. Did you call 'start()' first?");
        }
        this.output.write(request.getRequestMessage());
        this.output.flush();
        log.debug("Request sent of type {} with id {} : '{}'",
                request.getMessageType(),
                request.getMessageId(),
                request.getHexString());
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

    public boolean isRunning() {
        return this.thread != null && this.thread.isRunning();
    }

}
