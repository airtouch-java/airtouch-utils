package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.utils.HexString;
import airtouch.v5.builder.ZoneControlRequestBuilder;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.constant.ZoneControlConstants.ZoneControl;
import airtouch.v5.constant.ZoneControlConstants.ZonePower;
import airtouch.v5.constant.ZoneControlConstants.ZoneSetting;
import airtouch.v5.model.ZoneControlRequest;

public class ZoneControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondZone() {
        ZoneControlRequest zoneControlRequest = new ZoneControlRequestBuilder(1)
                .power(ZonePower.POWER_OFF)
                .build();

       
        Request<MessageType> request = ZoneControlHandler.generateRequest(15, zoneControlRequest);
        assertEquals("555555aa80b00fc0000c20000000000400010102000000e0".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPowerOffSecondZoneWithFluentBuilder() {

        ZoneControlRequest zoneControlRequest = ZoneControlHandler.requestBuilder(1)
                .power(ZonePower.POWER_OFF)
                .build();

        Request<MessageType> request = ZoneControlHandler.generateRequest(15, zoneControlRequest);
        assertEquals("555555aa80b00fc0000c20000000000400010102000000e0".toUpperCase(), request.getHexString());
    }
    
    @Test
    public void testGenerateRequestPercentageControlWithFluentBuilder() {
        
        ZoneControlRequest zoneControlRequest = ZoneControlHandler.requestBuilder(1)
                .control(ZoneControl.PERCENTAGE_CONTROL)
                .build();
        
        Request<MessageType> request = ZoneControlHandler.generateRequest(15, zoneControlRequest);
        assertEquals("555555aa80b00fc0000c2000000000040001011000000540".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestTemperatureControlWithFluentBuilder() {
        
        ZoneControlRequest zoneControlRequest = ZoneControlHandler.requestBuilder(1)
                .control(ZoneControl.TEMPERATURE_CONTROL)
                .build();
        
        Request<MessageType> request = ZoneControlHandler.generateRequest(15, zoneControlRequest);
        assertEquals("555555aa80b00fc0000c200000000004000101180000C7C1".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGenerateRequestPercentageSettingFirstZone() {
        ZoneControlRequest zoneControlRequest = new ZoneControlRequestBuilder(0)
                .setting(ZoneSetting.SET_OPEN_PERCENTAGE)
                .settingValue(80)
                .build();

        Request<MessageType> request = ZoneControlHandler.generateRequest(1, zoneControlRequest);
        assertEquals("555555aa80b001c0000c200000000004000100805000b0f9".toUpperCase(), request.getHexString());
    }
    
    @Test
    public void testGenerateRequestSetPointFirstZone() {
        ZoneControlRequest zoneControlRequest = new ZoneControlRequestBuilder(0)
                .setting(ZoneSetting.SET_TARGET_SETPOINT)
                .settingValue(25)
                .build();
        
        Request<MessageType> request = ZoneControlHandler.generateRequest(1, zoneControlRequest);
        assertEquals("555555aa80b001c0000c200000000004000100a09600daab".toUpperCase(), request.getHexString());
    }
    
    @Test
    public void determineHex() {
        byte byte2 = (byte) 0b10000000;
        System.out.println(HexString.fromBytes(new byte[] { 
                (byte) 0b00011000 }));
    }

}
