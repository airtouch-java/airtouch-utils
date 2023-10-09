package airtouch.v5.handler;

import java.util.ArrayList;
import java.util.List;

import airtouch.utils.ByteUtil;
import airtouch.Request;
import airtouch.ResponseList;
import airtouch.v5.constant.ZoneStatusConstants.ControlMethod;
import airtouch.v5.constant.ZoneStatusConstants.PowerState;
import airtouch.v5.AirTouchRequest;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.ControlOrStatusMessageSubType;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.SubMessageMetaData;
import airtouch.v5.model.ZoneStatusResponse;

public class ZoneStatusHandler extends AbstractHandler {

    public static Request<MessageConstants.Address> generateRequest(int messageId, Integer zoneNumber) {
        return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.CONTROL_OR_STATUS, ControlOrStatusMessageSubType.ZONE_STATUS);
    }

    /*
        Zone status message(0x2B)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~

        Data block received from AirTouch (8 bytes). See docs page 5.

        | Byte1 | Bit8-7 | Zone power state  | 00: Off, 01: On, 11: Turbo
        |       | Bit6-1 | Zone number       | 0-15
        | Byte2 | Bit8   | Control method    | 1: temperature control, 0: percentage control
        |       | Bit7-1 | Open percentage   | Current open percentage setting
        | Byte3 |        | Setpoint          | setpoint=(value+100)/10, 0xFF invalid
        | Byte4 | Bit8   | Sensor            | 1: has sensor, 0: no sensor
        |       | Bit7-1 |                   | NOT USED
        | Byte5 |        | Temperature       | Temperature=(value- 500)/10, temp>150 invalid
        | Byte6 |        |                   |
        | Byte7 | Bit8-3 |                   | NOT USED
        |       | Bit2   | Spill             | 1: Spill
        |       | Bit1   | Battery low       | 1: battery low, 0: normal
        | Byte8 |        |                   | NOT USED

    */

    /**
     * Parse the Zone Status data block. The data should already have been
     * checked to determine the message type and the CRC information removed.
     * @param subMessageMetaData
     *
     * @param airTouchDataBlock
     * @return a List of ZoneStatus objects. One for each zone message found.
     */
    public static ResponseList<ZoneStatusResponse> handle(SubMessageMetaData subMessageMetaData, int messageId, byte[] airTouchDataBlock) {
        checkHeaderIsRemoved(airTouchDataBlock);
        List<ZoneStatusResponse> zoneStatuses = new ArrayList<>();
        for (int i = 0; i <= subMessageMetaData.getRepeatDataCount(); i++) {
            int zoneOffset = i * 8;
            ZoneStatusResponse zoneStatus = new ZoneStatusResponse();
            zoneStatus.setPowerstate(PowerState.getFromByte(airTouchDataBlock[zoneOffset + 0]));
            zoneStatus.setZoneNumber(resolveZoneNumber(airTouchDataBlock[zoneOffset + 0]));
            zoneStatus.setControlMethod(ControlMethod.getFromByte(airTouchDataBlock[zoneOffset + 1]));
            zoneStatus.setOpenPercentage(determineOpenPercentage(airTouchDataBlock[zoneOffset + 1]));
            zoneStatus.setTargetSetpoint(determineTargetSetpoint(airTouchDataBlock[zoneOffset + 2]));
            zoneStatus.setHasTemperatureSensor(determineHasTemperatureSensor(airTouchDataBlock[zoneOffset + 3]));
            zoneStatus.setCurrentTemperature(determineCurrentTemperature(
                    airTouchDataBlock[zoneOffset + 4],
                    airTouchDataBlock[zoneOffset + 5]));
            zoneStatus.setSpill(determinerSpill(airTouchDataBlock[zoneOffset + 6]));
            zoneStatus.setBatteryLow(determineBatteryLow(airTouchDataBlock[zoneOffset + 6]));
            //zoneStatus.setTurboSupported(determineTurboSupported(airTouchDataBlock[zoneOffset + 6]));
            zoneStatuses.add(zoneStatus);
        }
        return new ResponseList<>(MessageType.ZONE_STATUS, messageId, zoneStatuses);
    }

    private static boolean determinerSpill(byte byte7) {
        // bitmask everything except the second to last bit.
        int isSpill = byte7 & 0b00000010;
        // Shift the bits right by 1 so that bit 2 becomes the LSB.
        isSpill = isSpill >> 1;
        return isSpill == 1;
    }

    private static Integer determineCurrentTemperature(byte byte5, byte byte6) {
        if (-1 == byte5) {  // TODO: Need to confirm that 0xFF == -1
            return null; // Current Temp is not available.
        }
        // Combine byte5, and byte6
        int temperature = ByteUtil.toInt(byte5, byte6);
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
        return ((byte3 & 0xff) + 100) /10;
    }

    private static boolean determineBatteryLow(byte byte7) {
        // bitmask everything except the bit.
        int batteryLow = byte7 & 0b00000001;
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

    private static int resolveZoneNumber(byte byte1) {
        // bitmask the first two bits, since we used them for the PowerState
        int zoneNumber = byte1 & 0b00111111;
        // Return the rest of the bits if they're within our expected range.
        if (zoneNumber >= 0 && zoneNumber <= 15) {
            return zoneNumber;
        }
        throw new IllegalArgumentException(String.format("Zone number outside allowable range. Must be from 0 to 15. Found zoneNumber was '%s'", zoneNumber));
    }

    private static int getZoneCount(byte[] airTouchDataBlock) {
        // Our data payload is 8 bytes per zone.
        // Check that our payload is a multiple of 8 bytes.
        if (airTouchDataBlock.length % 8 == 0) {
            return airTouchDataBlock.length / 8;
        }
        throw new IllegalArgumentException("ZoneStatus messageBlock is not a multiple of 8 bytes. Length is:" + airTouchDataBlock.length);

    }

}
