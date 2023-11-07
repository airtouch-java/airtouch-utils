package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.v4.builder.GroupControlRequestBuilder;
import airtouch.constant.ZoneControlConstants.ZoneControl;
import airtouch.constant.ZoneControlConstants.ZonePower;
import airtouch.v4.constant.MessageConstants;
import airtouch.v4.model.GroupControlRequest;

public class GroupControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondGroup() {
        GroupControlRequest groupControlRequest = new GroupControlRequestBuilder(1)
                .power(ZonePower.POWER_OFF)
                .build();

        Request<MessageConstants.Address> request = GroupControlHandler.generateRequest(1, groupControlRequest);
        assertEquals("555580b0012a000401020000da59".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPowerOffSecondGroupWithFluentBuilder() {

        GroupControlRequest groupControlRequest = GroupControlHandler.requestBuilder(1).power(ZonePower.POWER_OFF).build();

        Request<MessageConstants.Address> request = GroupControlHandler.generateRequest(1, groupControlRequest);
        assertEquals("555580b0012a000401020000da59".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPercetageControlFirstGroup() {
        GroupControlRequest groupControlRequest = new GroupControlRequestBuilder(0)
                .control(ZoneControl.PERCENTAGE_CONTROL)
                .build();

        Request<MessageConstants.Address> request = GroupControlHandler.generateRequest(1, groupControlRequest);
        assertEquals("555580b0012a00040010000023f8".toUpperCase(), request.getHexString());
    }

}
