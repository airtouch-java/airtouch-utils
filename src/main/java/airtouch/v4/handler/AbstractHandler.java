package airtouch.v4.handler;

import java.util.Arrays;

import airtouch.v4.constant.MessageConstants;
import airtouch.v4.utils.ByteUtil;

public abstract class AbstractHandler {

    /**
     * Determines if the dataBlock still contains the first two HEADER bytes.
     * Throws {@link IllegalArgumentException} if it does.
     * <p>
     * The byte array passed to a handler should only contain the data bytes.
     * Header, address, messageId, messageType, dataLength and CRC should have been removed.
     *
     * @param airTouchDataBlock
     * @throws IllegalArgumentException
     */
    protected static void checkHeaderIsRemoved(byte[] airTouchDataBlock) {
        if (isHeaderPresent(airTouchDataBlock)) {
            throw new IllegalArgumentException("Found header in message data. Please only send validated message data block, not full message.");
        }
    }
    
    /**
     * Determines if the dataBlock still contains the first two HEADER bytes.
     * Throws {@link IllegalArgumentException} if it does not.
     * <p>
     * The byte array passed should contain all the data bytes.
     *
     * @param airTouchDataBlock
     * @throws IllegalArgumentException
     */
    protected static void checkHeaderIsPresent(byte[] airTouchDataBlock) {
        if (! isHeaderPresent(airTouchDataBlock)) {
            throw new IllegalArgumentException("Header not found in message data. Please full message.");
        }
    }
    
    /**
     * Determines if the dataBlock still contains the first two HEADER bytes.
     * <p>
     * Return true if the byte array passed contains all the data bytes.
     *
     * @return true if the byte array passed contains all the data bytes, otherwise false.
     * @param airTouchDataBlock
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
