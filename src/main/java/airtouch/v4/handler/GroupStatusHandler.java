package airtouch.v4.handler;

import java.util.ArrayList;
import java.util.List;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.v4.AirTouchRequest;
import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.GroupStatusConstants.ControlMethod;
import airtouch.v4.constant.GroupStatusConstants.PowerState;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.GroupStatusResponse;

public class GroupStatusHandler extends AbstractHandler {

    public static Request<MessageType, MessageConstants.Address> generateRequest(int messageId, Integer groupNumber) {

        // Empty data array for group Status request.
        byte[] data = new byte[] {};
        return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.GROUP_STATUS, data);
    }

    /*
        Group status message(0x2B)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (6 bytes). See docs page 5.

        | Byte1 | Bit8-7 | Group power state | 00: Off, 01: On, 11: Turbo
        |       | Bit6-1 | Group number      | 0-15
        | Byte2 | Bit8   | Control method    | 1: temperature control, 0: percentage control
        |       | Bit7-1 | Open percentage   | Current open percentage setting
        | Byte3 | Bit8   | Battery low       | 1: battery low, 0: normal
        |       | Bit7   | Turbo support     | 1: Support turbo, 0: not support turbo
        |       | Bit6-1 | Target setpoint   | Current target setpoint setting
        | Byte4 | Bit8   | Sensor            | 1: has sensor, 0: no sensor
        |       | Bit7-1 |                   | NOT USED
        | Byte5 |        | Temperature       | Byte5=0xff indicates temp not available
        | Byte6 | Bit8-6 | (Total: 11Bits)   | Current Temperature = (VALUE - 500)/10
        |       | Bit5   | Spill             | 1: Spill
        |       | Bit4-1 |                   | NOT USED
    */

    /**
     * Parse the Group Status data block. The data should already have been
     * checked to determine the message type and the CRC information removed.
     *
     * @param airTouchDataBlock
     * @return a List of GroupStatus objects. One for each group message found.
     */
    public static ResponseList<GroupStatusResponse, MessageType> handle(int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);
        List<GroupStatusResponse> groupStatuses = new ArrayList<>();
        for (int i = 0; i < getGroupCount(airTouchDataBlock); i++) {
            int groupOffset = i * 6;
            GroupStatusResponse groupStatus = new GroupStatusResponse();
            groupStatus.setPowerstate(PowerState.getFromByte(airTouchDataBlock[groupOffset + 0]));
            groupStatus.setGroupNumber(resolveGroupNumber(airTouchDataBlock[groupOffset + 0]));
            groupStatus.setControlMethod(ControlMethod.getFromByte(airTouchDataBlock[groupOffset + 1]));
            groupStatus.setOpenPercentage(determineOpenPercentage(airTouchDataBlock[groupOffset + 1]));
            groupStatus.setBatteryLow(determineBatteryLow(airTouchDataBlock[groupOffset + 2]));
            groupStatus.setTurboSupported(determineTurboSupported(airTouchDataBlock[groupOffset + 2]));
            groupStatus.setTargetSetpoint(determineTargetSetpoint(airTouchDataBlock[groupOffset + 2]));
            groupStatus.setHasTemperatureSensor(determineHasTemperatureSensor(airTouchDataBlock[groupOffset + 3]));
            groupStatus.setCurrentTemperature(determineCurrentTemperature(
                    airTouchDataBlock[groupOffset + 4],
                    airTouchDataBlock[groupOffset + 5]));
            groupStatus.setSpill(determinerSpill(airTouchDataBlock[groupOffset + 5]));
            groupStatuses.add(groupStatus);
        }
        return new ResponseList<>(MessageType.GROUP_STATUS, messageId, groupStatuses);
    }

    private static boolean determinerSpill(byte byte6) {
        // bitmask everything except the fourth bit.
        int isSpill = byte6 & 0b00010000;
        // Shift the bits right by 4 so that bit 4 becomes the LSB.
        isSpill = isSpill >> 4;
        return isSpill == 1;
    }

    private static Integer determineCurrentTemperature(byte byte5, byte byte6) {
        if (-1 == byte5) {  // TODO: Need to confirm that 0xFF == -1
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

    private static boolean determineHasTemperatureSensor(byte byte4) {
        // bitmask everything except the first bit.
        int hasTemperatureSensor = byte4 & 0b10000000;
        // Shift the bits right by 7 so that our MSB becomes the LSB.
        hasTemperatureSensor = hasTemperatureSensor >> 7;
        return hasTemperatureSensor == 1;
    }

    private static int determineTargetSetpoint(byte byte3) {
        // bitmask the first two bits, since we used them for the batteryLow and turboSupported
        // Return the rest of the bits.
        return byte3 & 0b00111111;
    }

    private static boolean determineTurboSupported(byte byte3) {
        // bitmask everything except the second bit.
        int turboSupported = byte3 & 0b01000000;
        // Shift the bits right by 6 so that our MSB becomes the LSB.
        turboSupported = turboSupported >> 6;
        return turboSupported == 1;
    }

    private static boolean determineBatteryLow(byte byte3) {
        // bitmask everything except the first bit.
        int batteryLow = byte3 & 0b10000000;
        // Shift the bits right by 7 so that our MSB becomes the LSB.
        batteryLow = batteryLow >> 7;
        return batteryLow == 1;
    }

    private static int determineOpenPercentage(byte byte2) {
        // bitmask the first bit, since we used them for the ControlMethod
        int openPercentage = byte2 & 0b01111111;
        // Return the rest of the bits if they're within our expected range.
        if (openPercentage >= 0 && openPercentage <= 100) {
            return openPercentage;
        }
        throw new IllegalArgumentException(String.format("Open percentage outside allowable range. Must be from 0 to 100. Found openPercentage was '%s'", openPercentage));
    }

    private static int resolveGroupNumber(byte byte1) {
        // bitmask the first two bits, since we used them for the PowerState
        int groupNumber = byte1 & 0b00111111;
        // Return the rest of the bits if they're within our expected range.
        if (groupNumber >= 0 && groupNumber <= 15) {
            return groupNumber;
        }
        throw new IllegalArgumentException(String.format("Group number outside allowable range. Must be from 0 to 15. Found groupNumber was '%s'", groupNumber));
    }

    private static int getGroupCount(byte[] airTouchDataBlock) {
        // Our data payload is 6 bytes per group.
        // Check that our payload is a multiple of 6 bytes.
        if (airTouchDataBlock.length % 6 == 0) {
            return airTouchDataBlock.length / 6;
        }
        throw new IllegalArgumentException("GroupStatus messageBlock is not a multiple of 6 bytes");

    }

}
