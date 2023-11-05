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
    private static final int MESSAGE_HEADER_BYTES_LENGTH = 8;
    private static final int DEFAULT_BUFFER_SIZE = 255;
    private int byteCount = 0;
    private int dataLength = 0;
    private ByteBuffer byteBuffer;

    private MessageHolder(int byteCount, int dataLength, int bufferSize) {
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
     * Note: dataLength is determined from bytes 7 & 8 of the Airtouch4 message.
     * <p>
     * Use {@link MessageHolder#putByte(byte)} to add further bytes to this message.<br>
     * Call {@link MessageHolder#isFinished()} to determine if the number of bytes received
     * matched the expected message size (including header and checksum).
     *
     * @param bytes - The bytes received so far.
     * @param dataLength - The size of the data component of the message. Determined from bytes 7 & 8 of the Airtouch4 message
     * @return
     */
    public static MessageHolder initialiseWithData(Stack<Byte> bytes, int dataLength) {
        MessageHolder messageHolder = new MessageHolder(MESSAGE_HEADER_BYTES_LENGTH, dataLength, MESSAGE_HEADER_BYTES_LENGTH + dataLength + CHECKSUM_BYTES_LENGTH);
        for (int i = 0; i < MESSAGE_HEADER_BYTES_LENGTH; i++) {
            messageHolder.byteBuffer.put(bytes.get(i));
        }
        return messageHolder;
    }

    /**
     * Initialise the MessageHolder with a buffer large enough to start receiving a message.
     * This is typically called to create a new MessageHolder when starting to receive a new
     * message. Once bytes 7 & 8 have been received, call {@link MessageHolder#initialiseWithData(Stack, int)}
     * to create a new MessageHolder with the expected dataLength.
     *
     * @return A new MesssageHolder instance with a default size buffer.
     */
    public static MessageHolder initialiseEmpty() {
        return new MessageHolder(0, 0, DEFAULT_BUFFER_SIZE);
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
        return this.byteCount == MESSAGE_HEADER_BYTES_LENGTH + dataLength + CHECKSUM_BYTES_LENGTH;
    }

    public void incrementByteCount() {
        this.byteCount++;
    }

    public int getByteCount() {
        return byteCount;
    }
    public void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    /**
     * Get the expected data length of the message.
     * The dataLength is determined from bytes 7 and 8 of the message to/from Airtouch4.
     * @return the expected data length.
     */
    public int getDataLength() {
        return dataLength;
    }
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
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
