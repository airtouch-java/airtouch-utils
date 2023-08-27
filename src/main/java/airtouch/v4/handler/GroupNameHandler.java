package airtouch.v4.handler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.utils.HexString;
import airtouch.v4.AirTouchRequest;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.GroupNameResponse;

/**
 * Handler for GroupName extended format responses.
 *
 */
public class GroupNameHandler extends AbstractHandler {

    public static Request generateRequest(int messageId, Integer groupNumber) {

        if (groupNumber == null) { // No group number, so ask for all groups.
            // data array for Group Name request - 0xff 0x12.
            byte[] data = HexString.toByteArray("ff12");
            return new AirTouchRequest(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
        } else {
            // data array for Group Name request - 0xff 0x12 + group number (1 byte).
            byte[] data = { (byte) 0xff, (byte) 0x12, (byte) (groupNumber & 0xFF) };
            return new AirTouchRequest(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, data);
        }
    }

    /*
        Group Name Extended message(0xFF 0x12)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (variable number of bytes). See docs page 11.

        | Byte1    | Fixed 0xFF
        | Byte2    | Fixed 0x12
        | Byte3    | Group number | 0-15
        | Byte4-11 | Group name   | 8 bytes in total. If less than 8 bytes, pad with 0's.

     */

    public static ResponseList<GroupNameResponse, MessageType> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);
        List<GroupNameResponse> groupNames = new ArrayList<>();

        for (int i = 0; i < getGroupCount(airTouchDataBlock); i++) {
            int groupOffset = i * 9;
            GroupNameResponse groupName = new GroupNameResponse();
            groupName.setGroupNumber(airTouchDataBlock[groupOffset + 0]);
            String name = new String(stripNulls(Arrays.copyOfRange(airTouchDataBlock, groupOffset + 1, groupOffset +9)), StandardCharsets.US_ASCII);
            groupName.setName(name);
            groupNames.add(groupName);
        }
        return new ResponseList<>(MessageType.GROUP_NAME, messageId, groupNames);
    }

    private static int getGroupCount(byte[] airTouchDataBlock) {
        // Our data payload is 9 bytes per group.
        // Check that our payload is a multiple of 9 bytes.
        if (airTouchDataBlock.length % 9 == 0) {
            return airTouchDataBlock.length / 9;
        }
        throw new IllegalArgumentException("GroupName messageBlock is not a multiple of 9 bytes.");

    }
}
