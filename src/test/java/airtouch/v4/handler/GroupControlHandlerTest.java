package airtouch.v4.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.v4.Request;
import airtouch.v4.constant.GroupControlConstants.GroupControl;
import airtouch.v4.constant.GroupControlConstants.GroupPower;
import airtouch.v4.model.GroupControlRequest;

public class GroupControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondGroup() {
        GroupControlRequest groupControlRequest = new GroupControlHandler.RequestBuilder(1)
                .power(GroupPower.POWER_OFF)
                .build();

        Request request = GroupControlHandler.generateRequest(1, groupControlRequest);
        assertEquals("555580b0012a000401020000da59".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPowerOffSecondGroupWithFluentBuilder() {

        GroupControlRequest groupControlRequest = GroupControlHandler.requestBuilder(1).power(GroupPower.POWER_OFF).build();

        Request request = GroupControlHandler.generateRequest(1, groupControlRequest);
        assertEquals("555580b0012a000401020000da59".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPercetageControlFirstGroup() {
        GroupControlRequest groupControlRequest = new GroupControlHandler.RequestBuilder(0)
                .control(GroupControl.PERCENTAGE_CONTROL)
                .build();

        Request request = GroupControlHandler.generateRequest(1, groupControlRequest);
        assertEquals("555580b0012a00040010000023f8".toUpperCase(), request.getHexString());
    }

}
