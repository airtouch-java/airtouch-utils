package airtouch.v5.handler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.constant.AirConditionerControlConstants.Mode;
import airtouch.model.AirConditionerAbilityResponse;
import airtouch.utils.HexString;
import airtouch.v5.AirTouchRequest;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.ExtendedMessageType;
import airtouch.v5.constant.MessageConstants.MessageType;

/**
 * Handler for AirConditioner Ability responses<p>
 * Is invoked when a message from the Airtouch5 has been identified as an AirConditioner ability message.
 */
public class AirConditionerAbilityHandler extends AbstractHandler {

    private static final Logger log = LoggerFactory.getLogger(AirConditionerAbilityHandler.class);

    public static Request<MessageConstants.Address> generateRequest(int messageId, Integer acNumber) {

        if (acNumber == null) { // No acNumber number, so ask for all ACs.
            // data array for AC Ability Name request - 0xff 0x11.
            byte[] data = { (byte) 0xff, (byte) 0x11 };
            return new AirTouchRequest(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, ExtendedMessageType.AC_ABILITY, data);
        } else {
            // data array for AC Ability request - 0xff 0x11 + AC number (1 byte).
            byte[] data = { (byte) 0xff, (byte) 0x11, (byte) (acNumber & 0xFF) };
            return new AirTouchRequest(Address.EXTENDED_SEND, messageId, MessageType.EXTENDED, ExtendedMessageType.AC_ABILITY, data);
        }
    }

    /*
        Air-conditioner Ability Extended message(0xFF 0x12)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (variable number of bytes). See docs page 12.

        | Byte1    |        | Fixed 0xFF                  | Note: will have been removed in ExtendedMessageHandler
        | Byte2    |        | Fixed 0x11                  | Note: will have been removed in ExtendedMessageHandler
        | Byte3    |        | AC number                   | 0-7
        | Byte4    |        | Data length                 | This data shows the count of following bytes belong to
                                                          | the ability of this AC
        | Byte5-20 |        | AC Name                     | 24 bytes in total. If less than 24 bytes, end with 0.
        | Byte21   |        | Start Zone number           | First zone on this AC unit
        | Byte22   |        | Zone count
        | Byte23   | Bit8-6 | Not used
                   | Bit5   | Cool mode                   | 1: supported, 0: not supported
                   | Bit4   | Fan mode                    | 1: supported, 0: not supported
                   | Bit3   | Dry mode                    | 1: supported, 0: not supported
                   | Bit2   | Heat mode                   | 1: supported, 0: not supported
                   | Bit1   | Auto mode                   | 1: supported, 0: not supported
                                                          |
        | Byte24   | Bit8   | Fan speed Intelligent Auto  | 1: supported, 0: not supported
                   | Bit7   | Fan speed - Turbo           | 1: supported, 0: not supported
                   | Bit6   | Fan speed - Powerful        | 1: supported, 0: not supported
                   | Bit5   | Fan speed - High            | 1: supported, 0: not supported
                   | Bit4   | Fan speed - Medium          | 1: supported, 0: not supported
                   | Bit3   | Fan speed - Low             | 1: supported, 0: not supported
                   | Bit2   | Fan speed - Quiet           | 1: supported, 0: not supported
                   | Bit1   | Fan speed - Auto            | 1: supported, 0: not supported
                                                          |
        | Byte25   |        | Minimum set point
        | Byte26   |        | Maximum set point
        | Byte27   | Bit8   | Group display option        | Group8. 1: show, 0: hide
                   | Bit7   | Group display option        | Group7. 1: show, 0: hide
                   | Bit6   | Group display option        | Group6. 1: show, 0: hide
                   | Bit5   | Group display option        | Group5. 1: show, 0: hide
                   | Bit4   | Group display option        | Group4. 1: show, 0: hide
                   | Bit3   | Group display option        | Group3. 1: show, 0: hide
                   | Bit2   | Group display option        | Group2. 1: show, 0: hide
                   | Bit1   | Group display option        | Group1. 1: show, 0: hide
                                                          |
        | Byte28   | Bit8   | Group display option        | Group16. 1: show, 0: hide
                   | Bit7   | Group display option        | Group15. 1: show, 0: hide
                   | Bit6   | Group display option        | Group14. 1: show, 0: hide
                   | Bit5   | Group display option        | Group13. 1: show, 0: hide
                   | Bit4   | Group display option        | Group12. 1: show, 0: hide
                   | Bit3   | Group display option        | Group11. 1: show, 0: hide
                   | Bit2   | Group display option        | Group10. 1: show, 0: hide
                   | Bit1   | Group display option        | Group9.  1: show, 0: hide
    */

    /**
     * Parse the AC Ability data block. The data should already have been
     * checked to determine the message type and the CRC information removed.
     *
     * @param messageId - Integer of the messageId returned in the response
     * @param airTouchDataBlock - byte array of just the data part of the response from the Airtouch
     * @return a List of AC Ability objects. One for each AC message found.
     */
    public static ResponseList<AirConditionerAbilityResponse> handle(int messageId, byte[] airTouchDataBlock) {
        log.debug("Handling AirConditionerAbility message: {}", HexString.fromBytes(airTouchDataBlock));
        checkHeaderIsRemoved(airTouchDataBlock);
        List<AirConditionerAbilityResponse> acAbilities = new ArrayList<>();
        int dataLength = getDataLength(airTouchDataBlock);
        log.debug("dataLength is {} bytes per AC.", dataLength);
        for (int i = 0; i < getAcCount(airTouchDataBlock); i++) {
            // dataLength will either be 26 or 28.
            // So set our starting marker to be the loop iteration multiplied by the dataLength.
            int acOffset = i * dataLength;
            AirConditionerAbilityResponse acAbility = new AirConditionerAbilityResponse();

            // Remember, bytes 1 and 2 were removed, so byte3 is really byte1, but zero based, so it's '0'
            acAbility.setAcNumber(resolveAcNumber(airTouchDataBlock[acOffset + 0]));
            // We are ignoring byte4, aka 1. We already determined the datalength above.
            // Get bytes 5 though 20, aka, 2-17
            String name = new String(stripNulls(Arrays.copyOfRange(airTouchDataBlock, acOffset + 2, acOffset + 17)), StandardCharsets.US_ASCII);
            acAbility.setAcName(name);

            // byte21 is Group Start number, aka 18
            acAbility.setStartGroupNumber(airTouchDataBlock[acOffset + 18] & 0xFF);
            // byte22 is the count of groups, aka 19
            acAbility.setZoneCount(airTouchDataBlock[acOffset + 19] & 0xFF);
            // byte23 tells us which modes are supported. aka 20
            // The first 3 bits (8-6) are not used. The remaining 5 bits represent one Mode each.
            addIfModeIsSupported(acAbility, airTouchDataBlock[acOffset + 20], 0b00010000, Mode.COOL);
            addIfModeIsSupported(acAbility, airTouchDataBlock[acOffset + 20], 0b00001000, Mode.FAN);
            addIfModeIsSupported(acAbility, airTouchDataBlock[acOffset + 20], 0b00000100, Mode.DRY);
            addIfModeIsSupported(acAbility, airTouchDataBlock[acOffset + 20], 0b00000010, Mode.HEAT);
            addIfModeIsSupported(acAbility, airTouchDataBlock[acOffset + 20], 0b00000001, Mode.AUTO);

            // byte24 tells us which Fan Speeds are supported. aka 21
            // The 8 bits represent one Speed each.
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b10000000, FanSpeed.INTELLIGENT_AUTO);
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b01000000, FanSpeed.TURBO);
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b00100000, FanSpeed.POWERFUL);
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b00010000, FanSpeed.HIGH);
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b00001000, FanSpeed.MEDIUM);
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b00000100, FanSpeed.LOW);
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b00000010, FanSpeed.QUIET);
            addIfFanSpeedIsSupported(acAbility, airTouchDataBlock[acOffset + 21], 0b00000001, FanSpeed.AUTO);

            // byte25 and 26 are the min and max cool setpoint.
            acAbility.setMinCoolSetPoint(airTouchDataBlock[acOffset + 22] & 0xFF);
            acAbility.setMaxCoolSetPoint(airTouchDataBlock[acOffset + 23] & 0xFF);

            // byte27 and 28 are the min and max heat setpoint.
            acAbility.setMinHeatSetPoint(airTouchDataBlock[acOffset + 24] & 0xFF);
            acAbility.setMaxHeatSetPoint(airTouchDataBlock[acOffset + 25] & 0xFF);

            acAbilities.add(acAbility);
        }
        return new ResponseList<>(ExtendedMessageType.AC_ABILITY, messageId, acAbilities);
    }

    private static int getDataLength(byte[] airTouchDataBlock) {
        if (airTouchDataBlock.length % 24 == 0) {
            return 24;
        }
        if (airTouchDataBlock.length % 26 == 0) {
            return 26;
        }
        throw new IllegalArgumentException("AcAbility messageBlock is not a multiple of 26 bytes (or 24 bytes on older firmware). Length was " + airTouchDataBlock.length);
    }

    private static void addIfModeIsSupported(AirConditionerAbilityResponse acAbility, byte byte23, int bitmask, Mode acMode) {
        // zero everything except the bit from the bit mask.
        int modeBit = byte23 & bitmask;
        // if the modeBit still equals the bitmask, then the bit we care about is a '1'
        if (modeBit == bitmask) {
            acAbility.addSupportedMode(acMode);
        }
    }
    private static void addIfFanSpeedIsSupported(AirConditionerAbilityResponse acAbility, byte byte24, int bitmask, FanSpeed fanSpeed) {
        // zero everything except the bit from the bit mask.
        int fanSpeedBit = byte24 & bitmask;
        // if the fanSpeedBit still equals the bitmask, then the bit we care about is a '1'
        if (fanSpeedBit == bitmask) {
            acAbility.addSupportedFanSpeed(fanSpeed);
        }
    }

    private static int resolveAcNumber(byte byte1) {
        // bitmask the first two bits, since we used them for the PowerState
        int acNumber = byte1 & 0b00111111;
        // Return the rest of the bits if they're within our expected range.
        if (acNumber >= 0 && acNumber <= 3) {
            return acNumber;
        }
        throw new IllegalArgumentException(String.format("AC number outside allowable range. Must be from 0 to 3. Found acNumber was '%s'", acNumber));
    }

    private static int getAcCount(byte[] airTouchDataBlock) {
        // Our data payload is 22 bytes per unit on older firmware or 24 bytes since version 1.2.3.
        // Use that to determine how many AC units we have in the airTouchDataBlock.
        return airTouchDataBlock.length / getDataLength(airTouchDataBlock);
    }


}
