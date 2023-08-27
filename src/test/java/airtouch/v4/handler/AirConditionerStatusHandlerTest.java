package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.v4.ResponseList;
import airtouch.v4.constant.AirConditionerStatusConstants.Mode;
import airtouch.v4.constant.AirConditionerStatusConstants.PowerState;
import airtouch.v4.model.AirConditionerStatusResponse;
import airtouch.utils.HexString;

public class AirConditionerStatusHandlerTest {

    @Test
    public void testGeneratingAcStatusRequest() {
        Request request = AirConditionerStatusHandler.generateRequest(1, null);
        assertEquals("555580b0012d0000f4cf".toUpperCase(), request.getHexString());
    }
    
    @Test
    public void testHandleAcStatusResponse() {
        // This data is copied from AirTouch4 protocol doc page 8.
        // 5555 b080 01 2d 0010 40421a006180000001001a006180fffe cacb
        //                      ^--------- data block ---------^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "40421a006180000001001a006180fffe";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<AirConditionerStatusResponse> response = AirConditionerStatusHandler.handle(0, dataBlockBytes);
        System.out.println(response);

        assertEquals(2, response.size());
        AirConditionerStatusResponse acStatus01 = response.get(0);
        assertEquals(Mode.COOL, acStatus01.getMode());
        assertEquals(26, acStatus01.getTargetSetpoint());
        assertEquals(28, acStatus01.getCurrentTemperature().intValue());
        assertEquals(0, acStatus01.getErrorCode());
        
        AirConditionerStatusResponse acStatus02 = response.get(1);
        assertEquals(PowerState.OFF, acStatus02.getPowerstate());
        assertEquals(26, acStatus02.getTargetSetpoint());
        assertEquals(28, acStatus02.getCurrentTemperature().intValue());
        assertEquals(65534, acStatus02.getErrorCode());
        
    }
    
    @Test
    public void testBitShift() {
        Integer mode = 64 >> 4;
        System.out.println(Integer.toBinaryString(mode));
    }

}
