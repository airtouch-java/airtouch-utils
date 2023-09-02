package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.AirConditionerControlConstants.AcPower;
import airtouch.v5.constant.AirConditionerControlConstants.Mode;
import airtouch.v5.constant.AirConditionerControlConstants.SetpointControl;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.AirConditionerControlRequest;

public class AirConditionerControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondAc() {
        AirConditionerControlRequest acControlRequest = AirConditionerControlHandler.requestBuilder()
                .acNumber(1)
                .acPower(AcPower.POWER_OFF)
                .build();

        Request<MessageType, MessageConstants.Address> request = AirConditionerControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555555aa80b001c0000c220000000004000121ff00ffd347".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestSetFirstAcToCoolMode() {
        AirConditionerControlRequest acControlRequest = AirConditionerControlHandler.requestBuilder()
                .acNumber(0)
                .acMode(Mode.COOL)
                .build();
        AirConditionerControlRequest acControlRequest2 = AirConditionerControlHandler.requestBuilder()
                .acNumber(1)
                .setpointControl(SetpointControl.SET_TO_VALUE)
                .setpointValue(26)
                .build();

        Request<MessageType, MessageConstants.Address> request = AirConditionerControlHandler.generateRequest(1, acControlRequest, acControlRequest2);
        assertEquals("555555aa80b001c000102200000000040002004f00ff01ff40a0104b".toUpperCase(), request.getHexString());
    }

}
