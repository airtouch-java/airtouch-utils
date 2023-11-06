package airtouch.v4.constant;

import airtouch.constant.AirtouchConstant;

public class AirConditionerStatusConstants {

    public enum PowerState implements AirtouchConstant<airtouch.constant.AirConditionerStatusConstants.PowerState> {
        OFF(0x00, airtouch.constant.AirConditionerStatusConstants.PowerState.OFF),
        ON(0x01, airtouch.constant.AirConditionerStatusConstants.PowerState.ON),
        NOT_AVAILABLE(0x11, airtouch.constant.AirConditionerStatusConstants.PowerState.NOT_AVAILABLE);

        private int bytes;
        private airtouch.constant.AirConditionerStatusConstants.PowerState generic;

        PowerState(int bytes, airtouch.constant.AirConditionerStatusConstants.PowerState generic) {
            this.bytes = bytes;
            this.generic = generic;
        }

        public int getBytes() {
            return bytes;
        }

        @Override
        public airtouch.constant.AirConditionerStatusConstants.PowerState getGeneric() {
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
            } else {
                return NOT_AVAILABLE;
            }
        }
    }

    public enum Mode implements AirtouchConstant<airtouch.constant.AirConditionerStatusConstants.Mode> {

        AUTO         (0b0000, airtouch.constant.AirConditionerStatusConstants.Mode.AUTO),
        HEAT         (0b0001,  airtouch.constant.AirConditionerStatusConstants.Mode.HEAT),
        DRY          (0b0010, airtouch.constant.AirConditionerStatusConstants.Mode.DRY),
        FAN          (0b0011, airtouch.constant.AirConditionerStatusConstants.Mode.FAN),
        COOL         (0b0100, airtouch.constant.AirConditionerStatusConstants.Mode.COOL),
        AUTO_HEAT    (0b1000, airtouch.constant.AirConditionerStatusConstants.Mode.AUTO_HEAT),
        AUTO_COOL    (0b1001, airtouch.constant.AirConditionerStatusConstants.Mode.AUTO_COOL),
        NOT_AVAILABLE(0b1111, airtouch.constant.AirConditionerStatusConstants.Mode.NOT_AVAILABLE); // Anything that is not one of the above. Just using 1111 as a placeholder.

        private int bytes;
        private airtouch.constant.AirConditionerStatusConstants.Mode generic;

        Mode(int bytes, airtouch.constant.AirConditionerStatusConstants.Mode generic) {
            this.bytes = bytes;
            this.generic = generic;
        }

        public int getBytes() {
            return bytes;
        }

        @Override
        public airtouch.constant.AirConditionerStatusConstants.Mode getGeneric() {
            return this.generic;
        }

        public static Mode getFromByte(byte byte2) {
            // Mode is represented by bits 8-5 of Byte 2.
            // Apply a bit mask to zero out any bits we don't care about.
            int bitmask = 0b11110000; // We want just the MSB
            int mode = byte2 & bitmask;
            // Shift the bits right by 4 so that bits end on the LSB.
            mode = mode >> 4;

            // Our bits should now be right most.
            // Now it should equate to one of the states defined above.
            if (AUTO.getBytes() == mode) {
                return AUTO;
            } else if (HEAT.getBytes() == mode) {
                return HEAT;
            } else if (DRY.getBytes() == mode) {
                return DRY;
            } else if (FAN.getBytes() == mode) {
                return FAN;
            } else if (COOL.getBytes() == mode) {
                return COOL;
            } else if (AUTO_HEAT.getBytes() == mode) {
                return AUTO_HEAT;
            } else if (AUTO_COOL.getBytes() == mode) {
                return AUTO_COOL;
            } else {
                return NOT_AVAILABLE;
            }

        }
    }
    public enum FanSpeed implements AirtouchConstant<airtouch.constant.AirConditionerStatusConstants.FanSpeed> {

        AUTO         (0b0000, airtouch.constant.AirConditionerStatusConstants.FanSpeed.AUTO),
        QUIET        (0b0010, airtouch.constant.AirConditionerStatusConstants.FanSpeed.QUIET),
        LOW          (0b0010, airtouch.constant.AirConditionerStatusConstants.FanSpeed.LOW),
        MEDIUM       (0b0011, airtouch.constant.AirConditionerStatusConstants.FanSpeed.MEDIUM),
        HIGH         (0b0100, airtouch.constant.AirConditionerStatusConstants.FanSpeed.HIGH),
        POWERFUL     (0b0101, airtouch.constant.AirConditionerStatusConstants.FanSpeed.POWERFUL),
        TURBO        (0b0110, airtouch.constant.AirConditionerStatusConstants.FanSpeed.TURBO),
        NOT_AVAILABLE(0b1111, airtouch.constant.AirConditionerStatusConstants.FanSpeed.NOT_AVAILABLE); // Anything that is not one of the above. Just using 1111 as a placeholder.

        private int bytes;
        private airtouch.constant.AirConditionerStatusConstants.FanSpeed generic;

        FanSpeed(int bytes, airtouch.constant.AirConditionerStatusConstants.FanSpeed generic) {
            this.bytes = bytes;
            this.generic = generic;
        }

        public int getBytes() {
            return bytes;
        }

        @Override
        public airtouch.constant.AirConditionerStatusConstants.FanSpeed getGeneric() {
            return generic;
        }

        public static FanSpeed getFromByte(byte byte2) {
            // FanSpeed is represented by bits 4-1 of Byte 2.
            // Apply a bit mask to zero out any bits we don't care about.
            int bitmask = 0b00001111; // We want just the MSB
            int mode = byte2 & bitmask;

            // Now it should equate to one of the states defined above.
            if (AUTO.getBytes() == mode) {
                return AUTO;
            } else if (QUIET.getBytes() == mode) {
                return QUIET;
            } else if (LOW.getBytes() == mode) {
                return LOW;
            } else if (MEDIUM.getBytes() == mode) {
                return MEDIUM;
            } else if (HIGH.getBytes() == mode) {
                return HIGH;
            } else if (POWERFUL.getBytes() == mode) {
                return POWERFUL;
            } else if (TURBO.getBytes() == mode) {
                return TURBO;
            } else {
                return NOT_AVAILABLE;
            }

        }
    }
}
