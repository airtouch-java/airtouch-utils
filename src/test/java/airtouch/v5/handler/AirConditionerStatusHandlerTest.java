package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.model.AirConditionerStatusResponse;
import airtouch.utils.HexString;
import airtouch.constant.AirConditionerStatusConstants.FanSpeed;
import airtouch.constant.AirConditionerStatusConstants.Mode;
import airtouch.constant.AirConditionerStatusConstants.PowerState;
import airtouch.v5.constant.MessageConstants;

public class AirConditionerStatusHandlerTest {

    @Test
    public void testGeneratingAcStatusRequest() {
        Request<MessageConstants.Address> request = AirConditionerStatusHandler.generateRequest(1);
        assertEquals("555555AA80B001C0000823000000000000007DB0".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleAcStatusResponse() {
        // This data is copied from AirTouch5 protocol doc page 13 (labelled 10).
        //  555555AA B080 01 C0 001C 23000000000A0002 101278C002DA00008000014264C002E400008000 1234
        //                           ^-- su-btype --^ ^------------  data block  ------------^
        // 
        // Just pass in the data block. The rest should have been validated and removed earlier.
        String dataBlockHexString = "101278C002DA00008000014264C002E400008000";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<AirConditionerStatusResponse> responses = AirConditionerStatusHandler.handle(0, dataBlockBytes);
        System.out.println(responses);

        assertEquals(2, responses.size());
        
        AirConditionerStatusResponse acStatus01 = responses.get(0);
        assertEquals("First AC has wrong index", new Integer(0), acStatus01.getAcNumber());
        assertEquals("Unexpected target setpoint. ", 22, acStatus01.getTargetSetpoint());
        assertEquals( "CurrentTemperature unexpected value.", new Double(23.0), acStatus01.getCurrentTemperature());
        assertEquals( PowerState.ON, acStatus01.getPowerstate());
        assertEquals( Mode.HEAT, acStatus01.getMode());
        assertEquals( "FanSpeed unexpected value.", FanSpeed.LOW, acStatus01.getFanSpeed());
        assertEquals("Unexpected error code.", 0, acStatus01.getErrorCode());
        
        AirConditionerStatusResponse acStatus02= responses.get(1);
        assertEquals("Second AC has wrong index", new Integer(1), acStatus02.getAcNumber());
        assertEquals("Unexpected target setpoint. ", 20, (int)acStatus02.getTargetSetpoint());
        assertEquals("Unexpected temperature. ", new Double(24.0), acStatus02.getCurrentTemperature());
        assertEquals( PowerState.OFF, acStatus02.getPowerstate());
        assertEquals( Mode.COOL, acStatus02.getMode());
        assertEquals( FanSpeed.LOW, acStatus02.getFanSpeed());
        assertEquals("Unexpected error code.", 0, acStatus02.getErrorCode());

    }

    @Test
    public void testBitShift() {
        Integer mode = 64 >> 4;
        System.out.println(Integer.toBinaryString(mode));
    }

}
