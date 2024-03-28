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
        
        AirConditionerStatusResponse r1 = responses.get(0);
        assertEquals("First AC has wrong index", new Integer(0), r1.getAcNumber());
        assertEquals( 22, (int)r1.getTargetSetpoint());
        assertEquals( "CurrentTemperature unexpected value.", new Double(23.0), r1.getCurrentTemperature());
        assertEquals( PowerState.ON, r1.getPowerstate());
        assertEquals( Mode.HEAT, r1.getMode());
        assertEquals( "FanSpeed unexpected value.", FanSpeed.LOW, r1.getFanSpeed());
        assertEquals( 0, r1.getErrorCode());
        
        AirConditionerStatusResponse r2= responses.get(1);
        assertEquals("Second AC has wrong index", new Integer(1), r2.getAcNumber());
        assertEquals( 20, (int)r2.getTargetSetpoint());
        assertEquals( new Double(24.0), r2.getCurrentTemperature());
        assertEquals( PowerState.OFF, r2.getPowerstate());
        assertEquals( Mode.COOL, r2.getMode());
        assertEquals( FanSpeed.LOW, r2.getFanSpeed());
        assertEquals( 0, r2.getErrorCode());

    }

    @Test
    public void testBitShift() {
        Integer mode = 64 >> 4;
        System.out.println(Integer.toBinaryString(mode));
    }

}
