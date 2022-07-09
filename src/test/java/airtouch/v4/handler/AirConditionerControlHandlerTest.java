package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.v4.Request;
import airtouch.v4.constant.AirConditionerControlConstants.AcPower;
import airtouch.v4.constant.AirConditionerControlConstants.Mode;
import airtouch.v4.model.AirConditionerControlRequest;

public class AirConditionerControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondAc() {
        AirConditionerControlRequest acControlRequest = new AirConditionerControlHandler.RequestBuilder()
                .acNumber(0)
                .acPower(AcPower.POWER_OFF)
                .build();

        Request request = AirConditionerControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555580b0012c000481ff0000ea87".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestSetFirstAcToCoolMode() {
        AirConditionerControlRequest acControlRequest = AirConditionerControlHandler.requestBuilder()
                .acNumber(0)
                .acMode(Mode.COOL)
                .build();

        Request request = AirConditionerControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555580b0012c000400400000329e".toUpperCase(), request.getHexString());
    }

}
