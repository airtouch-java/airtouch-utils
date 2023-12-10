package airtouch.v5.model;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.utils.HexString;
import airtouch.v5.builder.AirConditionerControlRequestBuilder;
import airtouch.constant.AirConditionerControlConstants.AcPower;
import airtouch.constant.AirConditionerControlConstants.Mode;
import airtouch.constant.AirConditionerControlConstants.SetpointControl;

public class AirConditionerControlRequestTest {

    // Test that request assembles the bytes correctly
    // as per the example on page 8 of the V5 spec.
    // eg, 0x21 0xFF 0x00 0xFF
    @Test
    public void testSetSecondAcToOff() {
        AirConditionerControlRequest request = new AirConditionerControlRequestBuilder()
                .acNumber(1)
                .acPower(AcPower.POWER_OFF)
                .build();
        assertEquals("21FF00FF", HexString.fromBytes(request.getBytes()));
    }
    
    @Test
    public void testSetFirstAcToCool() {
        AirConditionerControlRequest request = new AirConditionerControlRequestBuilder()
                .acNumber(0)
                .acMode(Mode.COOL)
                .build();
        assertEquals("004F00FF", HexString.fromBytes(request.getBytes()));
    }
    
    @Test
    public void testSetSecondActo26Degrees() {
        AirConditionerControlRequest request = new AirConditionerControlRequestBuilder()
                .acNumber(1)
                .setpointControl(SetpointControl.SET_TO_VALUE)
                .setpointValue(26)
                .build();
        assertEquals("01FF40A0", HexString.fromBytes(request.getBytes()));
    }

}
