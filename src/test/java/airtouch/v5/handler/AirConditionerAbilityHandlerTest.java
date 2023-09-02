package airtouch.v5.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.v5.constant.AirConditionerControlConstants.Mode;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.AirConditionerAbilityResponse;
import airtouch.utils.HexString;

public class AirConditionerAbilityHandlerTest {

    @Test
    public void testGeneratingAbilityRequest() {
        Request<MessageType, MessageConstants.Address> request = AirConditionerAbilityHandler.generateRequest(1, null);
        assertEquals("555555aa90b0011f0002ff11834c".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGeneratingAcAbilityRequestForAcZero() {
        Request<MessageType, MessageConstants.Address> request = AirConditionerAbilityHandler.generateRequest(1, 0);
        assertEquals("555555aa90b0011f0003ff11000983".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleAcStatusResponse() {
        // This data is copied from AirTouch5 protocol doc page 13
        // 555555AA B090 01 1F 001A FF11 0018554E49540000000000000000000000000004171D101f121f checksum
        //                               ^------------------- data block -------------------^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "0018554E49540000000000000000000000000004171D101f121f";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<AirConditionerAbilityResponse, MessageType> response = AirConditionerAbilityHandler.handle(0, dataBlockBytes);
        System.out.println(response);

        assertEquals(1, response.size());
        AirConditionerAbilityResponse acAbility01 = response.get(0);
        assertEquals(0, acAbility01.getAcNumber());
        assertEquals("UNIT", acAbility01.getAcName());
        assertEquals(0, acAbility01.getStartGroupNumber());
        assertEquals(4, acAbility01.getGroupCount());

        assertFalse("Not expecting to find FAN mode", acAbility01.getSupportedModes().contains(Mode.FAN));
        assertTrue("Expected COOL mode but not found", acAbility01.getSupportedModes().contains(Mode.COOL));
        assertTrue("Expected HEAT mode but not found", acAbility01.getSupportedModes().contains(Mode.HEAT));
        assertTrue("Expected DRY mode but not found", acAbility01.getSupportedModes().contains(Mode.DRY));
        assertTrue("Expected AUTO mode but not found", acAbility01.getSupportedModes().contains(Mode.AUTO));

        assertTrue("Expecting to find LOW fanspeed but not found", acAbility01.getSupportedFanSpeeds().contains(FanSpeed.LOW));
        assertTrue("Expecting to find MEDIUM fanspeed but not found", acAbility01.getSupportedFanSpeeds().contains(FanSpeed.MEDIUM));
        assertTrue("Expecting to find HIGH fanspeed but not found", acAbility01.getSupportedFanSpeeds().contains(FanSpeed.HIGH));
        assertTrue("Expecting to find AUTO fanspeed but not found", acAbility01.getSupportedFanSpeeds().contains(FanSpeed.AUTO));
        assertFalse("Expecting to find QUIET fanspeed but not found", acAbility01.getSupportedFanSpeeds().contains(FanSpeed.QUIET));
        assertFalse("Expecting to find POWERFUL fanspeed but not found", acAbility01.getSupportedFanSpeeds().contains(FanSpeed.POWERFUL));
        assertFalse("Expecting to find TURBO fanspeed but not found", acAbility01.getSupportedFanSpeeds().contains(FanSpeed.TURBO));

        assertEquals(16, acAbility01.getMinCoolSetPoint());
        assertEquals(31, acAbility01.getMaxCoolSetPoint());
        assertEquals(18, acAbility01.getMinHeatSetPoint());
        assertEquals(31, acAbility01.getMaxHeatSetPoint());

    }
}
