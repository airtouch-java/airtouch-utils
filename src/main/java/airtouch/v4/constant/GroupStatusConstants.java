package airtouch.v4.constant;

import airtouch.constant.AirtouchConstant;
import airtouch.exception.UnknownAirtouchResponseException;

public class GroupStatusConstants {

    public enum PowerState  implements AirtouchConstant<airtouch.constant.ZoneStatusConstants.PowerState>{
        OFF(0x00, airtouch.constant.ZoneStatusConstants.PowerState.OFF),
        ON(0x01, airtouch.constant.ZoneStatusConstants.PowerState.ON),
        TURBO(0x11, airtouch.constant.ZoneStatusConstants.PowerState.TURBO);

        private int bytes;
        private airtouch.constant.ZoneStatusConstants.PowerState generic;

        PowerState(int bytes, airtouch.constant.ZoneStatusConstants.PowerState generic) {
            this.bytes = bytes;
            this.generic = generic;
        }

        public int getBytes() {
            return bytes;
        }

        public airtouch.constant.ZoneStatusConstants.PowerState getGeneric() {
            return generic;
        }

        public static PowerState getFromByte(byte byte1) {
            // PowerState is represented by bits 8 & 7 of Byte 1.
            // Apply a bit mask to zero out any bits we don't care about.
            int bitmask = 0b11000000; // We want just the to MSBs
            int powerState = byte1 & bitmask;
            // Shift the bits right by 6 so that our two MSBs become the LSBs.
            powerState = powerState >> 6;

            // Our two bits should now be right most.
            // Now they should equate to one of the states defined above.
            if (OFF.getBytes() == powerState) {
                return OFF;
            } else if (ON.getBytes() == powerState) {
                return ON;
            } else if (TURBO.getBytes() == powerState) {
                return TURBO;
            } else {
                throw new UnknownAirtouchResponseException(String.format(
                        "Unable to resolve PowerState from supplied byte. Supplied byte is: '%s'",
                        Integer.toHexString(powerState)));
            }

        }
    }

    public enum ControlMethod implements AirtouchConstant<airtouch.constant.ZoneStatusConstants.ControlMethod>{
        PERCENTAGE_CONTROL(0, airtouch.constant.ZoneStatusConstants.ControlMethod.PERCENTAGE_CONTROL),
        TEMPERATURE_CONTROL(1, airtouch.constant.ZoneStatusConstants.ControlMethod.TEMPERATURE_CONTROL);

        private int bytes;
        private airtouch.constant.ZoneStatusConstants.ControlMethod generic;

        ControlMethod(int bytes, airtouch.constant.ZoneStatusConstants.ControlMethod generic) {
            this.bytes = bytes;
            this.generic = generic;
        }

        public int getBytes() {
            return bytes;
        }
        public airtouch.constant.ZoneStatusConstants.ControlMethod getGeneric() {
            return generic;
        }

        public static ControlMethod getFromByte(byte byte2) {
            // ControlMethod is represented by bit 8 of Byte 2.
            // Apply a bit mask to zero out any bits we don't care about.
            int bitmask = 0b10000000; // We want just the MSB
            int controlMethod = byte2 & bitmask;
            // Shift the bits right by 7 so that our MSB becomes the LSB.
            controlMethod = controlMethod >> 7;

            // Our bit should now be right most.
            // Now it should equate to one of the states defined above.
            if (PERCENTAGE_CONTROL.getBytes() == controlMethod) {
                return PERCENTAGE_CONTROL;
            } else if (TEMPERATURE_CONTROL.getBytes() == controlMethod) {
                return TEMPERATURE_CONTROL;
            } else {
                throw new UnknownAirtouchResponseException(String.format(
                        "Unable to resolve ControlMethod from supplied byte. Supplied byte is: '%s'",
                        Integer.toHexString(controlMethod)));
            }

        }
    }
}
