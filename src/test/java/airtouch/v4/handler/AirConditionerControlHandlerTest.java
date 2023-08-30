package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.v4.constant.AirConditionerControlConstants.AcPower;
import airtouch.v4.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.v4.constant.AirConditionerControlConstants.Mode;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.AirConditionerControlRequest;

public class AirConditionerControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondAc() {
        AirConditionerControlRequest acControlRequest = AirConditionerControlHandler.requestBuilder()
                .acNumber(1)
                .acPower(AcPower.POWER_OFF)
                .build();

        Request<MessageType> request = AirConditionerControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555580b0012c000481ff3f001a96".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestSetFirstAcToCoolMode() {
        AirConditionerControlRequest acControlRequest = AirConditionerControlHandler.requestBuilder()
                .acNumber(0)
                .acMode(Mode.COOL)
                .build();

        Request<MessageType> request = AirConditionerControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555580b0012c0004004f3f00c1bf".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestSetFirstAcToCoolModeFanToAuto() {
        AirConditionerControlRequest acControlRequest = AirConditionerControlHandler.requestBuilder()
                .acNumber(0)
                .acMode(Mode.COOL)
                .fanSpeed(FanSpeed.AUTO)
                .build();

        Request<MessageType> request = AirConditionerControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555580b0012c000400403f00c28f".toUpperCase(), request.getHexString());
    }

}
