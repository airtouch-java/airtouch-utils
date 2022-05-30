package airtouch.v4.handler;

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
        long firstTwoBytes = ByteUtil.toLong(airTouchDataBlock, 0, 2);
        if (firstTwoBytes == MessageConstants.HEADER) {
            throw new IllegalArgumentException("Found header in message data. Please only send validated message data block, not full message to handlers");
        }
    }

}
