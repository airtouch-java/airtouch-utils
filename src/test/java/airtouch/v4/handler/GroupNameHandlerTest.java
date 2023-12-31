package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.utils.HexString;
import airtouch.v4.constant.MessageConstants;
import airtouch.model.ZoneNameResponse;

public class GroupNameHandlerTest {

    @Test
    public void testGeneratingGroupNameRequest() {
        Request<MessageConstants.Address> request = GroupNameHandler.generateRequest(1, null);
        assertEquals("555590b0011f0002ff12820c".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGeneratingGroupNameRequestForGroupZero() {
        Request<MessageConstants.Address> request = GroupNameHandler.generateRequest(1, 0);
        assertEquals("555590b0011f0003ff1200f983".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleGroup1NameResponse() {
        // This data is copied from AirTouch4 protocol doc page 15.
        // 5555 b090 01 1f 000b ff12 0047726f7570310000 fd18
        //                           ^-- data block --^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "0047726f7570310000";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ZoneNameResponse> response = GroupNameHandler.handle(0, dataBlockBytes);
        System.out.println("##" + response + "##");

        assertEquals(1, response.size());
        ZoneNameResponse acStatus01 = response.get(0);
        assertEquals(0, acStatus01.getZoneNumber());
        assertEquals("Group1", acStatus01.getName());

    }

    @Test
    public void testHandleAllGroupsNameResponse() {
        // This data is copied from AirTouch4 protocol doc page 15.
        // 5555 b090 01 1f 000b ff12 004c6976696e670000014b69746368656e0002426564726f6f6d00 3993
        //                           ^-------------------- data block --------------------^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "004c6976696e670000014b69746368656e0002426564726f6f6d00";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ZoneNameResponse> response = GroupNameHandler.handle(0, dataBlockBytes);
        System.out.println("##" + response + "##");

        assertEquals(3, response.size());
        ZoneNameResponse acStatus01 = response.get(0);
        assertEquals(0, acStatus01.getZoneNumber());
        assertEquals("Living", acStatus01.getName());

        ZoneNameResponse acStatus02 = response.get(1);
        assertEquals(1, acStatus02.getZoneNumber());
        assertEquals("Kitchen", acStatus02.getName());

        ZoneNameResponse acStatus03 = response.get(2);
        assertEquals(2, acStatus03.getZoneNumber());
        assertEquals("Bedroom", acStatus03.getName());

    }

}
