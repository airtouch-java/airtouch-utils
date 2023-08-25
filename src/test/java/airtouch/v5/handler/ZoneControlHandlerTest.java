package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;

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

       
        Request request = ZoneControlHandler.generateRequest(15, zoneControlRequest);
        assertEquals("555555aa80b00fc0000c20000000000400010102000000e0".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPowerOffSecondZoneWithFluentBuilder() {

        ZoneControlRequest zoneControlRequest = ZoneControlHandler.requestBuilder(1)
                .power(ZonePower.POWER_OFF)
                .build();

        Request request = ZoneControlHandler.generateRequest(15, zoneControlRequest);
        assertEquals("555555aa80b00fc0000c20000000000400010102000000e0".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPercetageControlFirstZone() {
        ZoneControlRequest zoneControlRequest = new ZoneControlRequestBuilder(0)
                .control(ZoneControl.PERCENTAGE_CONTROL)
                .settingValue(0)
                .build();

        Request request = ZoneControlHandler.generateRequest(15, zoneControlRequest);
        assertEquals("555555aa80b00fc0000c20000000000400010102ff00".toUpperCase(), request.getHexString());
    }
    
    @Test
    public void determineHex() {
        int byte2 = 0b10000000;
        System.out.println(byte2 & 0xFF);
    }

}
