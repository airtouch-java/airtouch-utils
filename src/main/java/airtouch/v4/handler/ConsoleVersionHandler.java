package airtouch.v4.handler;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.v4.Request;
import airtouch.v4.ResponseList;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.ConsoleVersionResponse;

/**
 * Handler for AirConditioner Ability responses<p>
 * Is invoked when a message from the Airtouch4 has been identified as an AirConditioner ability message.
 */
public class ConsoleVersionHandler extends AbstractHandler {

    private static final Logger log = LoggerFactory.getLogger(ConsoleVersionHandler.class);

    public static Request generateRequest(int messageId) {

        // data array for Console Version request - 0xff 0x30.
        byte[] data = { (byte) 0xff, (byte) 0x30 };
        return new Request(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
    }

    /*
        Group Name Extended message(0xFF 0x12)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (variable number of bytes). See docs page 12.

        | Byte1    |      | Fixed 0xFF
        | Byte2    |      | Fixed 0x30
        | Byte3    |      | Update Sign   | 0: latest version is installed
                                          | Other:  New version available.
        | Byte4    |      | Data length   | Version string length
        | Byte5... |      | Versions      | Two consoles separated by “|”. The first one is the master.

    */

    /**
     * Parse the AC Status data block. The data should already have been
     * checked to determine the message type and the CRC information removed.
     *
     * @param airTouchDataBlock
     * @return a List of AC Status objects. One for each AC message found.
     */
    public static ResponseList<ConsoleVersionResponse> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);

        ConsoleVersionResponse consoleVersionResponse = new ConsoleVersionResponse();
        consoleVersionResponse.setUpdateRequired(determineUpdateRequired(airTouchDataBlock[0]));

        // Byte 4 indicates the number of bytes to read in for a variable length field.
        // Because we have removed the header bytes, this is byte 2 (or 1 when zero based)
        int dataLength = airTouchDataBlock[1] & 0xFF;

        String versionString = new String(stripNulls(Arrays.copyOfRange(airTouchDataBlock, 2, dataLength + 2)), StandardCharsets.US_ASCII);
        consoleVersionResponse.setVersions(Arrays.asList(versionString.trim().split("\\|")));

        return new ResponseList<>(MessageType.AC_STATUS, messageId, Collections.singletonList(consoleVersionResponse));
    }

    private static boolean determineUpdateRequired(byte byte3) {
        int updateRequired = byte3 & 0xFF;
        return updateRequired != 0;
    }

}
