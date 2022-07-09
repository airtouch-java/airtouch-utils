package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.v4.Request;
import airtouch.v4.ResponseList;
import airtouch.v4.constant.AcStatusConstants.Mode;
import airtouch.v4.constant.AcStatusConstants.PowerState;
import airtouch.v4.model.AirConditionerStatusResponse;
import airtouch.v4.model.GroupNameResponse;
import airtouch.v4.utils.HexString;

public class GroupNameHandlerTest {

    @Test
    public void testGeneratingGroupNameRequest() {
        Request request = GroupNameHandler.generateRequest(1, null);
        assertEquals("555590b0011f0002ff12820c".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGeneratingGroupNameRequestForGroupZero() {
        Request request = GroupNameHandler.generateRequest(1, 0);
        assertEquals("555590b0011f0003ff1200f983".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleGroupNameResponse() {
        // This data is copied from AirTouch4 protocol doc page 15.
        // 5555 b090 01 1f 000b ff12 0047726f7570310000 fd18
        //                           ^-- data block --^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "0047726f7570310000";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<GroupNameResponse> response = GroupNameHandler.handle(0, dataBlockBytes);
        System.out.println("##" + response + "##");

        assertEquals(1, response.size());
        GroupNameResponse acStatus01 = response.get(0);
        assertEquals(0, acStatus01.getGroupNumber());
        assertEquals("Group1", acStatus01.getName());

    }

}
