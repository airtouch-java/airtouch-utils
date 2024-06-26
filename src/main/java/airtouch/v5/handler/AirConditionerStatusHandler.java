package airtouch.v5.handler;

import java.util.ArrayList;
import java.util.List;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.model.AirConditionerStatusResponse;
import airtouch.utils.ByteUtil;
import airtouch.v5.AirTouchRequest;
import airtouch.v5.constant.AirConditionerStatusConstants.FanSpeed;
import airtouch.v5.constant.AirConditionerStatusConstants.Mode;
import airtouch.v5.constant.AirConditionerStatusConstants.PowerState;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.ControlOrStatusMessageSubType;
import airtouch.v5.constant.MessageConstants.MessageType;

/**
 * Handler for AirConditioner Status responses<p>
 * Is invoked when a message from the Airtouch5 has been identified as an AirConditioner status message.
 */
public class AirConditionerStatusHandler extends AbstractControlHandler {

    public static Request<MessageConstants.Address> generateRequest(int messageId) {
        return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.CONTROL_OR_STATUS, ControlOrStatusMessageSubType.AC_STATUS);
    }

    /*
        AC status message(0x2D)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (8 bytes). See docs page 8.

        | Byte1 | Bit8-5 | AC power state | 0000: Off
                                          | 0001: On
                                          | 0010: Away(Off) 
                                          | 0011: Away(On) 
                                          | 0101: Sleep
                                          | Other: Not available
        |       | Bit4-1 | AC number      | 0-7
        | Byte2 | Bit8-5 | AC mode        | 0000: auto
                                          | 0001: heat
                                          | 0010: dry
                                          | 0011: fan
                                          | 0100: cool
                                          | 1000: auto heat
                                          | 1001: auto cool
                                          | Other: Not available
                                          |
        |       | Bit4-1 | AC fan speed   | 0000: auto
                                          | 0001: quiet
                                          | 0010: low
                                          | 0011: med
                                          | 0100: high
                                          | 0101: powerful
                                          | 0110: turbo
                                          | Other: Not available
                                          |
        | Byte3 |        | Target setpoint| Current target setpoint setting
        | Byte3 | Bit8   | Spill          | 1: Spill active, 0: Spill not active
                | Bit7   | AC Timer       | 1: Timer set, 0: Timer not set
        | Byte4 | Bits8-5                 | NOT USED
                | Bit4   | Turbo          | 1: Turbo active, 0: Turbo inactive
                | Bit3   | Bypass         | 1: Bypass active, 0: Bypass inactive
                | Bit2   | Spill          | 1: Spill active, 0: Spill inactive
                | Bit1   | Timer status   | 1: Timer set, 0: Timer not set
        | Byte5 |        | Temperature    | 0-2000: Temperature=(VALUE – 500)/10., Not available
        | Byte6 |        |                | Outside ths range, Not available.
        | Byte7 |        | Error Code     | 0 means no error.
        | Byte8 |        |                | Other codes mean there is an error about this AC.
        | Byte9 |                         | NOT USED
        | Byte10|                         | NOT USED


    */

    /**
     * Parse the AC Status data block. The data should already have been
     * checked to determine the message type and the CRC information removed.
     *
     * @param messageId - Integer of the messageId returned in the response
     * @param airTouchDataBlock - byte array of just the data part of the response from the Airtouch
     * @return a List of AC Status objects. One for each AC message found.
     */
    public static ResponseList<AirConditionerStatusResponse> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);
        List<AirConditionerStatusResponse> acStatuses = new ArrayList<>();
        for (int i = 0; i < getAcCount(airTouchDataBlock); i++) {
            int acOffset = i * 10;
            AirConditionerStatusResponse acStatus = new AirConditionerStatusResponse();
            acStatus.setPowerstate(PowerState.getFromByte(airTouchDataBlock[acOffset + 0]).getGeneric());
            acStatus.setAcNumber(resolveAcNumber(airTouchDataBlock[acOffset + 0]));
            acStatus.setMode(Mode.getFromByte(airTouchDataBlock[acOffset + 1]).getGeneric());
            acStatus.setFanSpeed(FanSpeed.getFromByte(airTouchDataBlock[acOffset + 1]).getGeneric());
            acStatus.setTargetSetpoint(determineTargetSetpoint(airTouchDataBlock[acOffset + 2]));
            acStatus.setSpill(determineSpill(airTouchDataBlock[acOffset + 3]));
            acStatus.setAcTimer(determineAcTimer(airTouchDataBlock[acOffset + 3]));
            acStatus.setCurrentTemperature(determineCurrentTemperature(
                    airTouchDataBlock[acOffset + 4],
                    airTouchDataBlock[acOffset + 5]));

            acStatus.setErrorCode(determineErrorCode(
                    airTouchDataBlock[acOffset + 6],
                    airTouchDataBlock[acOffset + 7]));
            acStatuses.add(acStatus);
        }
        return new ResponseList<>(MessageType.AC_STATUS, messageId, acStatuses);
    }

    private static boolean determineSpill(byte byte3) {
        // bitmask everything except the first bit.
        int isSpill = byte3 & 0b10000000;
        // Shift the bits right by 7 so that bit 1 becomes the LSB.
        isSpill = isSpill >> 7;
        return isSpill == 1;
    }

    private static boolean determineAcTimer(byte byte3) {
        // bitmask everything except the second bit.
        int isAcTimer = byte3 & 0b01000000;
        // Shift the bits right by 6 so that bit 2 becomes the LSB.
        isAcTimer = isAcTimer >> 6;
        return isAcTimer == 1;
    }

    private static Double determineCurrentTemperature(byte byte5, byte byte6) {
        if (-1 == byte5) {
            return null; // Current Temp is not available.
        }
        // Combine byte5, and byte6
        int temperatureUpper8bits = byte5 << 8;   // Move up 8 bits
        int temperatureLower8bits = byte6 & 0xFF; // convert to int
        int temperature = temperatureUpper8bits | temperatureLower8bits;   // OR together, so we have a 16bit value
        // Get value from bytes, subtract 500 and then divide by 10.
        return (temperature-500d)/10;
    }

    private static Integer determineErrorCode(byte byte5, byte byte6) {
        return ByteUtil.toInt(byte5, byte6);
    }

    private static int determineTargetSetpoint(byte byte3) {
        int targetTemperature = (byte3 & 0xFF);
        return (targetTemperature + 100)/10;
    }

    private static int resolveAcNumber(byte byte1) {
        // bitmask the first four bits, since we used them for the PowerState
        int acNumber = byte1 & 0b00001111;
        // Return the rest of the bits if they're within our expected range.
        if (acNumber >= 0 && acNumber <= 7) {
            return acNumber;
        }
        throw new IllegalArgumentException(String.format("AC number outside allowable range. Must be from 0 to 7. Found acNumber was '%s'", acNumber));
    }

    private static int getAcCount(byte[] airTouchDataBlock) {
        // Our data payload is 10 bytes per unit.
        // Check that our payload is a multiple of 10 bytes.
        if (airTouchDataBlock.length % 10 == 0) {
            return airTouchDataBlock.length / 10;
        }
        throw new IllegalArgumentException("AcStatus messageBlock is not a multiple of 10 bytes");

    }


}
