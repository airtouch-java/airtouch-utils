package airtouch.v5.handler;

import java.util.Arrays;

import airtouch.v5.constant.MessageConstants;
import airtouch.v5.model.SubMessageMetaData;
import airtouch.utils.ByteUtil;

public abstract class AbstractHandler {

    protected AbstractHandler() {}

    /**
     * Determines if the dataBlock still contains the first four HEADER bytes.
     * Throws {@link IllegalArgumentException} if it does.
     * <p>
     * The byte array passed to a handler should only contain the data bytes.
     * Header, address, messageId, messageType, dataLength and CRC should have been removed.
     *
     * @param airTouchDataBlock to check for headers
     * @throws IllegalArgumentException if the airTouchDataBlock still contains headers
     */
    protected static void checkHeaderIsRemoved(byte[] airTouchDataBlock) {
        if (isHeaderPresent(airTouchDataBlock)) {
            throw new IllegalArgumentException("Found header in message data. Please only send validated message data block, not full message.");
        }
    }

    /**
     * Determines if the dataBlock still contains the first four HEADER bytes.
     * Throws {@link IllegalArgumentException} if it does not.
     * <p>
     * The byte array passed should contain all the data bytes.
     *
     * @param airTouchDataBlock to check for headers
     * @throws IllegalArgumentException if the airTouchDataBlock does NOT contain headers
     */
    protected static void checkHeaderIsPresent(byte[] airTouchDataBlock) {
        if (! isHeaderPresent(airTouchDataBlock)) {
            throw new IllegalArgumentException("Header not found in message data. Please use full message.");
        }
    }


    /**
     * Determines if the dataBlock's first two bytes match the valueToMatch value.
     * Throws {@link IllegalArgumentException} if they do not.
     * <p>
     * The byte array passed should contain all the data bytes including the message type.
     *
     * @param valueToMatch - The value expected to find in the first two bytes of airTouchDataBlock
     * @param airTouchDataBlock - The byte array whose first two bytes should match the valueToMatch
     * @throws IllegalArgumentException if the airTouchDataBlock does NOT contain headers
     */
    protected static void checkMessageTypeMatchFirstTwoBytes(int valueToMatch, byte[] airTouchDataBlock) {
        if (! checkMessageTypeMatchesFirstTwoBytes(valueToMatch, airTouchDataBlock)) {
            throw new IllegalArgumentException("First two bytes not found in message data. Expected " + Integer. toHexString(valueToMatch));
        }
    }
    private static boolean checkMessageTypeMatchesFirstTwoBytes(int valueToMatch, byte[] airTouchDataBlock) {
        long firstTwoBytes = ByteUtil.toLong(airTouchDataBlock, 0, 2);
        return firstTwoBytes == valueToMatch;

    }

    /**
     * Determines if the dataBlock still contains the first four HEADER bytes.
     * <p>
     * Return true if the byte array passed contains all the data bytes.
     *
     * @return true if the byte array passed contains all the data bytes, otherwise false.
     * @param airTouchDataBlock to check that it starts with headers
     */
    private static boolean isHeaderPresent(byte[] airTouchDataBlock) {
        long firstFourBytes = ByteUtil.toLong(airTouchDataBlock, 0, 4);
        return firstFourBytes == MessageConstants.HEADER;
    }

    protected static boolean verifySubTypeData(SubMessageMetaData subMessageMetaData, byte[] subMessageDataBlock) {
        if (subMessageDataBlock.length % subMessageMetaData.getRepeatDataCount() == 0) {
            return true;
        }
        throw new IllegalArgumentException(String.format("subMessageDataBlock is not a multiple of %s bytes. Length is: %s",
                subMessageMetaData.getEachRepeatDataLength(), subMessageDataBlock.length));

    }

    /**
     * Strip out the trailing null (0x00) bytes in an Airtouch message. <br>
     * Certain messages are padded with 0x00 where the field is 
     * fixed length, but the content is variable. This method strips out all the trailing bytes 
     * by copying the array and returning only the values up until the first 0x00.
     * @param allBytesIncludingNulls - a byteArray that might contain null bytes
     * @return a copy of the byte array truncated to the point at which the first 0x00 is found.
     */
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
