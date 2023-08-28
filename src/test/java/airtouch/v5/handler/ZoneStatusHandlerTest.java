package airtouch.v5.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.constant.ZoneStatusConstants.PowerState;
import airtouch.v5.model.SubMessageMetaData;
import airtouch.v5.model.ZoneStatusResponse;
import airtouch.utils.HexString;

public class ZoneStatusHandlerTest {

    @Test
    public void testGeneratingZoneStatusRequest() {
        //555555AA80B001C000082100000000000000
        
        Request<MessageType> request = ZoneStatusHandler.generateRequest(1, 0);
        assertEquals("555555aa80b001c000082100000000000000a431".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleZoneStatusResponse() {
        // This data is copied from AirTouch5 protocol doc page 9 (labeled 6).
        // 555555AA B080 01 C0 0018 2100000000080001 4080968002E700000164FF0007FF0000
        //                                           ^-----      data block    -----^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "4080968002E700000164FF0007FF0000";
        byte[] subTypeMetaData = HexString.toByteArray("2100000000080001");
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        SubMessageMetaData subMessageMetaData = MessageHandler.determineSubMessageMetaData(subTypeMetaData);
        ResponseList<ZoneStatusResponse, MessageType> responses = ZoneStatusHandler.handle(subMessageMetaData, 1, dataBlockBytes);
        assertEquals(2, responses.size());
        ZoneStatusResponse response1 = responses.get(0);
        assertEquals(PowerState.ON, response1.getPowerstate());
        assertEquals(0, response1.getZoneNumber());
        assertEquals(0, response1.getOpenPercentage());
        
        ZoneStatusResponse response2 = responses.get(1);
        assertEquals(PowerState.OFF, response2.getPowerstate());
        assertEquals(1, response2.getZoneNumber());
        assertEquals(100, response2.getOpenPercentage());
    }

}
