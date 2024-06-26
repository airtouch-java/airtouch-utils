package airtouch.v4.handler;

import java.util.Arrays;

import airtouch.exception.IllegalAirtouchResponseException;
import airtouch.v4.constant.MessageConstants;
import airtouch.utils.ByteUtil;

public abstract class AbstractHandler {

    protected AbstractHandler() {}

    /**
     * Determines if the dataBlock still contains the first two HEADER bytes.
     * Throws {@link IllegalArgumentException} if it does.
     * <p>
     * The byte array passed to a handler should only contain the data bytes.
     * Header, address, messageId, messageType, dataLength and CRC should have been removed.
     *
     * @param airTouchDataBlock to check for headers
     * @throws IllegalArgumentException if header is found in message data
     */
    protected static void checkHeaderIsRemoved(byte[] airTouchDataBlock) {
        if (isHeaderPresent(airTouchDataBlock)) {
            throw new IllegalAirtouchResponseException("Found header in message data. Please only send validated message data block, not full message.");
        }
    }

    /**
     * Determines if the dataBlock still contains the first two HEADER bytes.
     * Throws {@link IllegalArgumentException} if it does not.
     * <p>
     * The byte array passed should contain all the data bytes.
     *
     * @param airTouchDataBlock to check for headers
     * @throws IllegalArgumentException if header is NOT found in message data
     */
    protected static void checkHeaderIsPresent(byte[] airTouchDataBlock) {
        if (! isHeaderPresent(airTouchDataBlock)) {
            throw new IllegalAirtouchResponseException("Header not found in message data. Please full message.");
        }
    }

    /**
     * Determines if the dataBlock still contains the first two HEADER bytes.
     * <p>
     * Return true if the byte array passed contains all the data bytes.
     *
     * @param airTouchDataBlock to check for headers
     * @return true if the byte array passed contains all the data bytes, otherwise false.
     */
    protected static boolean isHeaderPresent(byte[] airTouchDataBlock) {
        long firstTwoBytes = ByteUtil.toLong(airTouchDataBlock, 0, 2);
        return firstTwoBytes == MessageConstants.HEADER;
    }

    protected static byte[] stripNulls(byte[] allBytesIncludingNulls) {
        int length = allBytesIncludingNulls.length;
        for (int i = 0; i < allBytesIncludingNulls.length; i++) {
            byte b = allBytesIncludingNulls[i];
            if(b == 0x00) {
                length = i;
                break;
            }
        }
        return Arrays.copyOfRange(allBytesIncludingNulls, 0, length);
    }

}
