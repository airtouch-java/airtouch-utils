package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.utils.HexString;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.model.AirConditionerErrorResponse;

public class AirConditionerErrorHandlerTest {

    @Test
    public void testGeneratingAirConditionerErrorRequestForGroupZero() {
        Request<MessageConstants.Address> request = AirConditionerErrorHandler.generateRequest(1, 0);
        assertEquals("555555aa90b0011f0003ff10009982".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleGAirConditioner0ErrorResponse() {
        // This data is copied from AirTouch5 protocol doc page 16 (labeled as 13).
        // 555555aa b090 01 1f 001a ff10 000845523a2046464645 60d3
        //                               ^--- data block ---^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "000845523a2046464645";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<AirConditionerErrorResponse> response = AirConditionerErrorHandler.handle(0, dataBlockBytes);
        System.out.println("##" + response + "##");

        assertEquals(1, response.size());
        AirConditionerErrorResponse acStatus01 = response.get(0);
        assertEquals(0, acStatus01.getAcNumber());
        assertTrue(acStatus01.isErrored());
        assertEquals("ER: FFFE", acStatus01.getErrorMessage());

    }

}
