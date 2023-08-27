
package airtouch.v4.handler;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import airtouch.Request;
import airtouch.utils.HexString;
import airtouch.v4.ResponseList;
import airtouch.v4.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.v4.constant.AirConditionerControlConstants.Mode;
import airtouch.v4.model.AirConditionerAbilityResponse;

public class AirConditionerAbilityHandlerTest {

    @Test
    public void testGeneratingAbilityRequest() {
        Request request = AirConditionerAbilityHandler.generateRequest(1, null);
        assertEquals("555590b0011f0002ff11834C".toUpperCase(), request.getHexString());
    }

    @Test
    public void testGeneratingAcAbilityRequestForAcZero() {
        Request request = AirConditionerAbilityHandler.generateRequest(1, 0);
        assertEquals("555590b0011f0003ff11000983".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleAcStatusResponse() {
        // This data is copied from AirTouch4 protocol doc page 10.
        // 5555 b090 01 1f 001a ff11 0016554e49540000000000000000000000000004171d111f0700 dfbc
        //                           ^------------------- data block -------------------^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "0016554e49540000000000000000000000000004171d111f0700";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<AirConditionerAbilityResponse> response = AirConditionerAbilityHandler.handle(0, dataBlockBytes);
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

        assertEquals(17, acAbility01.getMinSetPoint());
        assertEquals(31, acAbility01.getMaxSetPoint());

    }
}
