package airtouch.v5.connector;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.Response;
import airtouch.ResponseCallback;
import airtouch.connector.AirtouchConnectorThread;
import airtouch.exception.AirtouchMessagingException;
import airtouch.exception.AirtouchResponseCrcException;
import airtouch.exception.IllegalAirtouchResponseException;
import airtouch.exception.UnknownAirtouchResponseException;
import airtouch.utils.ByteUtil;
import airtouch.utils.SizedStack;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.handler.MessageHandler;
import airtouch.internal.MessageHolder;

public class Airtouch5ConnectorThread extends Thread implements Runnable, AirtouchConnectorThread {

    private static final String AIRTOUCH_MESSAGE_HAS_BAD_CRC = "Airtouch message has bad CRC: '{}'";
    private static final String IGNORING_ILLEGAL_MESSAGE = "Ignoring illegal message: '{}'";
    private static final String IGNORING_UNKNOWN_MESSAGE = "Ignoring unknown message: '{}'";
    private static final String DEFAULT_THREAD_NAME = Airtouch5ConnectorThread.class.getSimpleName();

    private final Logger log = LoggerFactory.getLogger(Airtouch5ConnectorThread.class);

    private boolean stopping;
    private final InputStream input;
    private final ResponseCallback responseCallback;


    public Airtouch5ConnectorThread(final InputStream input, final ResponseCallback responseCallback) {
        super(DEFAULT_THREAD_NAME);
        this.input = input;
        this.responseCallback = responseCallback;
    }

    public Airtouch5ConnectorThread(final InputStream input, final ResponseCallback responseCallback, String threadName) {
        super(threadName);
        this.input = input;
        this.responseCallback = responseCallback;
    }

    public void shutdown() {
        this.stopping = true;
    }

    public boolean isRunning() {
        return !this.stopping;
    }

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
                if (messageHolder.isFinished()) {
                    try {
                        handleFinishedMessage(messageHandler, messageHolder);
                    } catch (IllegalArgumentException ex) {
                        log.warn("Could not handle finished message ", ex);
                    }
                    if (log.isTraceEnabled()) {
                        log.trace("Initalising MessageHolder to empty");
                    }
                    messageHolder = MessageHolder.initialiseEmpty();
                }
            }
        } catch (SocketException e) {
            log.debug("Socket exception: {}", e.getMessage(), e);
            // we are expecting a socket exception because we have closed it.
        } catch (UnknownAirtouchResponseException | IllegalAirtouchResponseException | AirtouchResponseCrcException e) {
            throw new AirtouchMessagingException("Exception during Airtouch response reading.", e);
        } catch (IOException e) {
            throw new AirtouchMessagingException("IOException during Airtouch response reading.", e);
        }
    }

    private void handleFinishedMessage(MessageHandler messageHandler, MessageHolder messageHolder) {
        try {
            Response response = messageHandler.handle(messageHolder.getBytes());
            if (log.isDebugEnabled()) {
                log.debug("Received response: '{}'. Sending to ", response);
            }
            responseCallback.handleResponse(response);
        } catch (UnknownAirtouchResponseException ex) {
            log.info(IGNORING_UNKNOWN_MESSAGE, ex.getMessage());
            if (log.isDebugEnabled()) {
                log.debug(IGNORING_UNKNOWN_MESSAGE, ex.getMessage(), ex);
            }
        } catch (IllegalAirtouchResponseException ex) {
            log.info(IGNORING_ILLEGAL_MESSAGE, ex.getMessage());
            if (log.isDebugEnabled()) {
                log.debug(IGNORING_ILLEGAL_MESSAGE, ex.getMessage(), ex);
            }
        } catch (AirtouchResponseCrcException ex) {
            log.info(AIRTOUCH_MESSAGE_HAS_BAD_CRC, ex.getMessage());
            if (log.isDebugEnabled()) {
                log.debug(AIRTOUCH_MESSAGE_HAS_BAD_CRC, ex.getMessage(), ex);
            }
        }
    }

    private boolean messageStarted(SizedStack<Byte> bytes) {
        try {
            if (bytes.size() < 8) return false;
            if (ByteUtil.toInt(bytes.get(0), bytes.get(1)) != MessageConstants.HEADER) return false;
            Address address = Address.getFromBytes(ByteUtil.toInt(bytes.get(2), bytes.get(3)));
            return ByteUtil.toInt(bytes.get(0), bytes.get(1)) == MessageConstants.HEADER
                    && (address.equals(Address.STANDARD_RECEIVE) || address.equals(Address.EXTENDED_RECEIVE));
        } catch (UnknownAirtouchResponseException ex) {
            log.info(IGNORING_UNKNOWN_MESSAGE, ex.getMessage());
            if (log.isDebugEnabled()) {
                log.debug(IGNORING_UNKNOWN_MESSAGE, ex.getMessage(), ex);
            }
            return false;
        }
    }

}
