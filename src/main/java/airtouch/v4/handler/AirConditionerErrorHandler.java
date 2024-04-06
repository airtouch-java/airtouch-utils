package airtouch.v4.handler;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.model.AirConditionerErrorResponse;
import airtouch.v4.AirTouchRequest;
import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;

/**
 * Handler for AirConditioner Error responses<p>
 * Is invoked when a message from the Airtouch4 has been identified as an AirConditioner error message.
 */
public class AirConditionerErrorHandler extends AbstractHandler {

    public static Request<MessageConstants.Address> generateRequest(int messageId, int acNumber) {

        // data array for Error request - 0xff 0x10 .
        byte[] data = { (byte) 0xff, (byte) 0x10, (byte) (acNumber & 0xFF)} ;
        return new AirTouchRequest(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
    }

    /*
        AC Error Extended message(0xFF 0x10)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (variable number of bytes). See docs page 14.

        | Byte1    |      | Fixed 0xFF
        | Byte2    |      | Fixed 0x10
        | Byte3    |      | AC Number         | 0 - 3
        | Byte4    |      | Error info length | Error info length(If no error, will be 0)
        | Byte5... |      | Error info        | Error as string

    */

    /**
     * Parse the AC Error data block. The data should already have been
     * checked to determine the message type and the CRC information removed.
     *
     * @param messageId - ID for this message
     * @param airTouchDataBlock - byte array of data without header
     * @return a List containing one AC Error object.
     */
    public static ResponseList<AirConditionerErrorResponse> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);

        AirConditionerErrorResponse acErrorResponse = new AirConditionerErrorResponse();
        acErrorResponse.setAcNumber(airTouchDataBlock[0] & 0xFF);

        // Byte 4 indicates the number of bytes to read in for a variable length field.
        // Because we have removed the header bytes, this is byte 2 (or 1 when zero based)
        int dataLength = airTouchDataBlock[1] & 0xFF;

        // If error message length is greater than 0, we have an error.
        acErrorResponse.setErrored(dataLength > 0);

        if (dataLength > 0) {
            String errorString = new String(stripNulls(Arrays.copyOfRange(airTouchDataBlock, 2, dataLength + 2)), StandardCharsets.US_ASCII);
            acErrorResponse.setErrorMessage(errorString.trim());
        }

        return new ResponseList<>(MessageType.AC_STATUS, messageId, Collections.singletonList(acErrorResponse));
    }

}
