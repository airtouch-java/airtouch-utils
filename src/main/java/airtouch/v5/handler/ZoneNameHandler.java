package airtouch.v5.handler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import airtouch.v5.Request;
import airtouch.v5.ResponseList;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.ZoneNameResponse;
import airtouch.utils.HexString;

/**
 * Handler for ZoneName extended format responses.
 *
 */
public class ZoneNameHandler extends AbstractHandler {

    public static Request generateRequest(int messageId, Integer zoneNumber) {

        if (zoneNumber == null) { // No zone number, so ask for all zones.
            // data array for Zone Name request - 0xff 0x13.
            byte[] data = HexString.toByteArray("ff13");
            return new Request(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
        } else {
            // data array for Zone Name request - 0xff 0x13 + zone number (1 byte).
            byte[] data = { (byte) 0xff, (byte) 0x13, (byte) (zoneNumber & 0xFF) };
            return new Request(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
        }
    }

    /*
        Zone Name Extended message(0xFF 0x13)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (variable number of bytes). See docs page 11.

        | Byte1    | Fixed 0xFF
        | Byte2    | Fixed 0x12
        | Byte3    | Zone number | 0-15
        | Byte4-11 | Zone name   | 8 bytes in total. If less than 8 bytes, pad with 0's.

     */

    public static ResponseList<ZoneNameResponse> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);
        List<ZoneNameResponse> zoneNames = new ArrayList<>();

        for (int i = 0; i < getZoneCount(airTouchDataBlock); i++) {
            int zoneOffset = i * 9;
            ZoneNameResponse zoneName = new ZoneNameResponse();
            zoneName.setZoneNumber(airTouchDataBlock[zoneOffset + 0]);
            String name = new String(stripNulls(Arrays.copyOfRange(airTouchDataBlock, zoneOffset + 1, zoneOffset +9)), StandardCharsets.US_ASCII);
            zoneName.setName(name);
            zoneNames.add(zoneName);
        }
        return new ResponseList<>(MessageType.ZONE_NAME, messageId, zoneNames);
    }

    private static int getZoneCount(byte[] airTouchDataBlock) {
        // Our data payload is 9 bytes per zone.
        // Check that our payload is a multiple of 9 bytes.
        if (airTouchDataBlock.length % 9 == 0) {
            return airTouchDataBlock.length / 9;
        }
        throw new IllegalArgumentException("ZoneName messageBlock is not a multiple of 9 bytes.");

    }
}
