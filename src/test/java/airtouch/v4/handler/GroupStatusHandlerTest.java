package airtouch.v4.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.v4.Request;
import airtouch.v4.ResponseList;
import airtouch.v4.model.GroupStatus;
import airtouch.v4.utils.HexString;

public class GroupStatusHandlerTest {

    @Test
    public void testGeneratingGroupStatusRequest() {
        GroupStatusHandler groupStatusHandler = new GroupStatusHandler();
        Request request = groupStatusHandler.generateRequest(1, 0);
        assertEquals("555580b0012b0000f52f".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleGroupStatusResponse() {
        // This data is copied from AirTouch4 protocol doc page 8.
        // 5555 b080 01 2b 000c 40640000ff0041e41a806180 6579
        //                      ^----- data block -----^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "40640000ff0041e41a806180";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        GroupStatusHandler groupStatusHandler = new GroupStatusHandler();
        ResponseList<GroupStatus> response = groupStatusHandler.handle(0, dataBlockBytes);
        assertEquals(2, response.size());
    }

}
