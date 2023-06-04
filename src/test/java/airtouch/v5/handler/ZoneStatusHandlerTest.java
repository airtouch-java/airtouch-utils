package airtouch.v5.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.v5.Request;
import airtouch.v5.ResponseList;
import airtouch.v5.model.ZoneStatusResponse;
import airtouch.utils.HexString;

public class ZoneStatusHandlerTest {

    @Test
    public void testGeneratingZoneStatusRequest() {
        Request request = ZoneStatusHandler.generateRequest(1, 0);
        assertEquals("555555AA80B001C000082100000000000000A431".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleZoneStatusResponse() {
        // This data is copied from AirTouch4 protocol doc page 8.
        // 555555AA B080 01 C0 0018 21000000000800014080968002E700000164FF0007FF0000
        //                          ^-----             data block             -----^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "21000000000800014080968002E700000164FF0007FF0000";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ZoneStatusResponse> response = ZoneStatusHandler.handle(0, dataBlockBytes);
        assertEquals(2, response.size());
    }

}
