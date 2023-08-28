package airtouch.v4.handler;

import java.util.ArrayList;
import java.util.List;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.utils.ByteUtil;
import airtouch.v4.AirTouchRequest;
import airtouch.v4.constant.AirConditionerStatusConstants.FanSpeed;
import airtouch.v4.constant.AirConditionerStatusConstants.Mode;
import airtouch.v4.constant.AirConditionerStatusConstants.PowerState;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.AirConditionerStatusResponse;

/**
 * Handler for AirConditioner Status responses<p>
 * Is invoked when a message from the Airtouch4 has been identified as an AirConditioner status message.
 */
public class AirConditionerStatusHandler extends AbstractHandler {

    public static Request<MessageType> generateRequest(int messageId, Integer acNumber) {

        if (acNumber == null) { // No AC number, so ask for all ACs.
            // Empty data array for AC Status request.
            byte[] data = new byte[] {};
            return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.AC_STATUS, data);
        } else {
            // Data array for AC Status request for specific AC unit.
            byte[] data = { (byte) (acNumber & 0xFF) };
            return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.AC_STATUS, data);
        }
    }

    /*
        AC status message(0x2D)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (8 bytes). See docs page 8.

        | Byte1 | Bit8-7 | AC power state | 00: Off, 01: On, 10/11: Not available
        |       | Bit6-1 | AC number      | 0-3
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
        | Byte3 | Bit8   | Spill          | 1: Spill active, 0: Spill not active
                | Bit7   | AC Timer       | 1: Timer set, 0: Timer not set
                | Bit6-1 | Target setpoint| Current target setpoint setting
        | Byte4 |                         | NOT USED
        | Byte5 | Temperature             | Byte5=0xff, Not available
        | Byte6 | Bit8-6 | (Total: 11Bits)| Current Temperature = (VALUE - 500)/10
                | Bit5-1                  | NOT USED
        | Byte7 |        | Error Code     | 0 means no error.
        | Byte8          |                | Other codes mean there is an error about this AC.

    */

    /**
     * Parse the AC Status data block. The data should already have been
     * checked to determine the message type and the CRC information removed.
     *
     * @param airTouchDataBlock
     * @return a List of AC Status objects. One for each AC message found.
     */
    public static ResponseList<AirConditionerStatusResponse, MessageType> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);
        List<AirConditionerStatusResponse> acStatuses = new ArrayList<>();
        for (int i = 0; i < getAcCount(airTouchDataBlock); i++) {
            int acOffset = i * 8;
            AirConditionerStatusResponse acStatus = new AirConditionerStatusResponse();
            acStatus.setPowerstate(PowerState.getFromByte(airTouchDataBlock[acOffset + 0]));
            acStatus.setAcNumber(resolveAcNumber(airTouchDataBlock[acOffset + 0]));
            acStatus.setMode(Mode.getFromByte(airTouchDataBlock[acOffset + 1]));
            acStatus.setFanSpeed(FanSpeed.getFromByte(airTouchDataBlock[acOffset + 1]));
            acStatus.setSpill(determineSpill(airTouchDataBlock[acOffset + 2]));
            acStatus.setAcTimer(determineAcTimer(airTouchDataBlock[acOffset + 2]));
            acStatus.setTargetSetpoint(determineTargetSetpoint(airTouchDataBlock[acOffset + 2]));
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

    private static Integer determineCurrentTemperature(byte byte5, byte byte6) {
        if (-1 == byte5) {
            return null; // Current Temp is not available.
        }
        // Combine byte5, and the 3 MSBs from byte6
        int temperatureUpper8bits = byte5 << 3;
        int temperatureLower3bits = byte6 & 0b11100000;
        temperatureLower3bits = temperatureLower3bits>>> 5;
        int temperature = temperatureUpper8bits | temperatureLower3bits;
        // Get value from byte, subtract 500 and then divide by 10.
        return (temperature-500)/10;
    }

    private static Integer determineErrorCode(byte byte5, byte byte6) {
        return ByteUtil.toInt(byte5, byte6);
    }

    private static int determineTargetSetpoint(byte byte3) {
        // bitmask the first two bits, since we used them for the spill and acTimer
        // Return the rest of the bits.
        return byte3 & 0b00111111;
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
        // Our data payload is 8 bytes per unit.
        // Check that our payload is a multiple of 8 bytes.
        if (airTouchDataBlock.length % 8 == 0) {
            return airTouchDataBlock.length / 8;
        }
        throw new IllegalArgumentException("AcStatus messageBlock is not a multiple of 8 bytes");

    }


}
