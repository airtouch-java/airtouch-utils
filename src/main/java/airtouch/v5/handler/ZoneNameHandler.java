package airtouch.v5.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import airtouch.utils.HexString;
import airtouch.v5.AirTouchRequest;
import airtouch.Request;
import airtouch.ResponseList;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.ZoneNameResponse;

/**
 * Handler for ZoneName extended format responses.
 *
 */
public class ZoneNameHandler extends AbstractHandler {

    public static Request<MessageType> generateRequest(int messageId, Integer zoneNumber) {

        if (zoneNumber == null) { // No zone number, so ask for all zones.
            // data array for Zone Name request - 0xff 0x13.
            byte[] data = HexString.toByteArray("ff13");
            return new AirTouchRequest(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
        } else {
            // data array for Zone Name request - 0xff 0x13 + zone number (1 byte).
            byte[] data = { (byte) 0xff, (byte) 0x13, (byte) (zoneNumber & 0xFF) };
            return new AirTouchRequest(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
        }
    }

    /*
        Zone Name Extended message(0xFF 0x13)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (variable number of bytes). See docs page 17.

        | Byte1    | Fixed 0xFF   <- This will have been removed already 
        | Byte2    | Fixed 0x13   <- This will have been removed already
        | Byte3    | Zone number | 0-15
        | Byte4    | Name length
        | Byte5-.. | Zone name

     */

    public static ResponseList<ZoneNameResponse, MessageType> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);
        List<ZoneNameResponse> zoneNames = new ArrayList<>();
        
        int zoneOffset = 0;
        while (zoneOffset != airTouchDataBlock.length) {
            ZoneNameResponse zoneName = new ZoneNameResponse();
            zoneName.setZoneNumber(airTouchDataBlock[zoneOffset + 0]);
            int zoneNameLength = airTouchDataBlock[zoneOffset + 1];
            // Using the zoneNameLength, extract the expected number of bytes of data.
            String name = new String(Arrays.copyOfRange(airTouchDataBlock, zoneOffset + 2,  zoneOffset + zoneNameLength + 2));
            zoneName.setName(name);
            zoneNames.add(zoneName);
            zoneOffset = zoneOffset + 2 + zoneNameLength;
        }

        return new ResponseList<>(MessageType.ZONE_NAME, messageId, zoneNames);
    }

}
