package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.v5.Request;
import airtouch.v5.ResponseList;
import airtouch.v5.model.ZoneNameResponse;
import airtouch.utils.HexString;

public class ZoneNameHandlerTest {

    @Test
    public void testGeneratingZoneNameRequest() {
        Request request = ZoneNameHandler.generateRequest(1, null);
        assertEquals("555555aa90b0011f0002ff1342CD".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGeneratingZoneNameRequestForZoneZero() {
        Request request = ZoneNameHandler.generateRequest(1, 0);
        assertEquals("555555aa90b0011f0003ff13006982".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleZone1NameResponse() {
        // This data is copied from AirTouch4 protocol doc page 17 (labeled 14).
        // 555555aa b090 01 1f 000a ff13 00064c6976696e67 b62f
        //                               ^- data block -^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "00064c6976696e67";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ZoneNameResponse> response = ZoneNameHandler.handle(0, dataBlockBytes);
        System.out.println("##" + response + "##");

        assertEquals(1, response.size());
        ZoneNameResponse acStatus01 = response.get(0);
        assertEquals(0, acStatus01.getZoneNumber());
        assertEquals("Zone1", acStatus01.getName());

    }

    @Test
    public void testHandleAllZonesNameResponse() {
        // This data is copied from AirTouch4 protocol doc page 15.
        // 5555 b090 01 1f 000b ff12 004c6976696e670000014b69746368656e0002426564726f6f6d00 3993
        //                           ^-------------------- data block --------------------^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "004c6976696e670000014b69746368656e0002426564726f6f6d00";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ZoneNameResponse> response = ZoneNameHandler.handle(0, dataBlockBytes);
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
