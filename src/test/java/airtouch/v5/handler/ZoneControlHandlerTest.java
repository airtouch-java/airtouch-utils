package airtouch.v5.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.v5.Request;
import airtouch.v5.builder.ZoneControlRequestBuilder;
import airtouch.v5.constant.ZoneControlConstants.ZoneControl;
import airtouch.v5.constant.ZoneControlConstants.ZonePower;
import airtouch.v5.model.ZoneControlRequest;

public class ZoneControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondZone() {
        ZoneControlRequest zoneControlRequest = new ZoneControlRequestBuilder(1)
                .power(ZonePower.POWER_OFF)
                .build();

       
        Request request = ZoneControlHandler.generateRequest(1, zoneControlRequest);
        assertEquals("555555AA80B00FC0000C20000000000400010102FF00".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPowerOffSecondZoneWithFluentBuilder() {

        ZoneControlRequest zoneControlRequest = ZoneControlHandler.requestBuilder(1)
                .power(ZonePower.POWER_OFF)
                .build();

        Request request = ZoneControlHandler.generateRequest(1, zoneControlRequest);
        assertEquals("555555aa80b0012a000401020000da59".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPercetageControlFirstZone() {
        ZoneControlRequest zoneControlRequest = new ZoneControlRequestBuilder(0)
                .control(ZoneControl.PERCENTAGE_CONTROL)
                .build();

        Request request = ZoneControlHandler.generateRequest(1, zoneControlRequest);
        assertEquals("555555aa80b0012a00040010000023f8".toUpperCase(), request.getHexString());
    }

}
