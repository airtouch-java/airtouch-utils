package airtouch.internal;

import java.nio.ByteBuffer;
import java.util.Stack;

/**
 * Holds a message whilst it's being read in from the Airtouch4 response.
 *
 * Has methods to determine if the message has finished and allows
 * getting the content of the message.
 */
public class MessageHolder {

    private static final int CHECKSUM_BYTES_LENGTH = 2;
    private static final int DEFAULT_BUFFER_SIZE = 2550;
    private int byteCount = 0;
    private int dataLength = 0;
    private ByteBuffer byteBuffer;
    private int headerByteCount = 8;

    private MessageHolder(int headerByteCount, int byteCount, int dataLength, int bufferSize) {
        this.headerByteCount  = headerByteCount;
        this.byteCount = byteCount;
        this.dataLength = dataLength;
        this.byteBuffer = ByteBuffer.allocate(bufferSize);
    }

    public void putByte(byte character) {
        byteBuffer.put(character);
        this.byteCount++;
    }

    /**
     * Create a {@link MessageHolder} instance using the bytes received and with the expected
     * dataLength.
     * Note: dataLength is determined from bytes 7 &amp; 8 of the Airtouch4 message.
     * <p>
     * Use {@link MessageHolder#putByte(byte)} to add further bytes to this message.<br>
     * Call {@link MessageHolder#isFinished()} to determine if the number of bytes received
     * matched the expected message size (including header and checksum).
     *
     * @param headerByteCount - Size of the header bytes. eg 8 for AirTouch4 or 10 for Airtouch5
     * @param bytes - The bytes received so far.
     * @param dataLength - The size of the data component of the message. Determined from bytes 7 &amp; 8 of the Airtouch4 message
     * @return A new {@link MessageHolder} of the correct size with the <code>bytes</code> copied in.
     */
    public static MessageHolder initialiseWithData(int headerByteCount, Stack<Byte> bytes, int dataLength) {
        MessageHolder messageHolder = new MessageHolder(headerByteCount, headerByteCount, dataLength, headerByteCount + dataLength + CHECKSUM_BYTES_LENGTH);
        for (int i = 0; i < headerByteCount; i++) {
            messageHolder.byteBuffer.put(bytes.get(i));
        }
        return messageHolder;
    }

    /**
     * Initialise the MessageHolder with a buffer large enough to start receiving a message.
     * This is typically called to create a new MessageHolder when starting to receive a new
     * message. Once bytes 7 &amp; 8 have been received, call {@link MessageHolder#initialiseWithData(int, Stack, int)}
     * to create a new MessageHolder with the expected dataLength.
     *
     * @param headerByteCount - Size of the header bytes. eg 8 for AirTouch4 or 10 for Airtouch5
     * @return A new MesssageHolder instance with a default size buffer.
     */
    public static MessageHolder initialiseEmpty(int headerByteCount) {
        return new MessageHolder(headerByteCount, 0, 0, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Determines if the message is received all the bytes.
     *
     * Compares the number of bytes we have received with the expected data size.
     * Accounts for the checksum bytes and header bytes from the message to determine
     * if the number of bytes received equates to the whole message.
     *
     * @return Whether all expected bytes have been read for this message.
     */
    public boolean isFinished() {
        return this.byteCount == this.headerByteCount + dataLength + CHECKSUM_BYTES_LENGTH;
    }

    /**
     * Get the byte[] that backs the buffer.
     * Note: changes to the buffer will be reflected in this byte array.
     *
     * @return A reference to the byte array that backs the buffer.
     */
    public byte[] getBytes() {
        return this.byteBuffer.array();
    }
}
