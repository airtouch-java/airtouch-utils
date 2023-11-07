package airtouch.v4.constant;

import airtouch.constant.AirtouchConstant;

public class AirConditionerControlConstants {

    public enum AcPower implements AirtouchConstant<airtouch.constant.AirConditionerControlConstants.AcPower> {
        //                      ||     Bits 8 to 7 of byte 1
        NO_CHANGE            (0b00000000, airtouch.constant.AirConditionerControlConstants.AcPower.NO_CHANGE),  // 00
        NEXT_POWER_STATE     (0b01000000, airtouch.constant.AirConditionerControlConstants.AcPower.NEXT_POWER_STATE),  // 01
        POWER_OFF            (0b10000000, airtouch.constant.AirConditionerControlConstants.AcPower.POWER_OFF),  // 10
        POWER_ON             (0b11000000, airtouch.constant.AirConditionerControlConstants.AcPower.POWER_ON);  // 11

        private int bits;
        private airtouch.constant.AirConditionerControlConstants.AcPower generic;

        AcPower(int bits, airtouch.constant.AirConditionerControlConstants.AcPower generic) {
            this.bits = bits;
            this.generic = generic;
        }

        public int getBits() {
            return bits;
        }

        @Override
        public airtouch.constant.AirConditionerControlConstants.AcPower getGeneric() {
            return this.generic;
        }
    }

    public enum Mode implements AirtouchConstant<airtouch.constant.AirConditionerControlConstants.Mode> {
        //              ||||    Bits 8 to 5 of byte 2
        AUTO         (0b00000000, airtouch.constant.AirConditionerControlConstants.Mode.AUTO),
        HEAT         (0b00010000, airtouch.constant.AirConditionerControlConstants.Mode.HEAT),
        DRY          (0b00100000, airtouch.constant.AirConditionerControlConstants.Mode.DRY),
        FAN          (0b00110000, airtouch.constant.AirConditionerControlConstants.Mode.FAN),
        COOL         (0b01000000, airtouch.constant.AirConditionerControlConstants.Mode.COOL),
        NO_CHANGE    (0b11110000, airtouch.constant.AirConditionerControlConstants.Mode.NO_CHANGE); // Anything that is not one of the above. Just using 1111 as a placeholder.

        private int bits;
        private airtouch.constant.AirConditionerControlConstants.Mode generic;

        Mode(int bytes, airtouch.constant.AirConditionerControlConstants.Mode generic) {
            this.bits = bytes;
            this.generic = generic;
        }

        public int getBits() {
            return bits;
        }

        @Override
        public airtouch.constant.AirConditionerControlConstants.Mode getGeneric() {
            return generic;
        }

    }

    public enum FanSpeed implements AirtouchConstant<airtouch.constant.AirConditionerControlConstants.FanSpeed> {
        //                  ||||    Bits 4 to 1 of byte 2
        AUTO         (0b00000000, airtouch.constant.AirConditionerControlConstants.FanSpeed.AUTO),
        QUIET        (0b00000010, airtouch.constant.AirConditionerControlConstants.FanSpeed.QUIET),
        LOW          (0b00000010, airtouch.constant.AirConditionerControlConstants.FanSpeed.LOW),
        MEDIUM       (0b00000011, airtouch.constant.AirConditionerControlConstants.FanSpeed.MEDIUM),
        HIGH         (0b00000100, airtouch.constant.AirConditionerControlConstants.FanSpeed.HIGH),
        POWERFUL     (0b00000101, airtouch.constant.AirConditionerControlConstants.FanSpeed.POWERFUL),
        TURBO        (0b00000110, airtouch.constant.AirConditionerControlConstants.FanSpeed.TURBO),
        NO_CHANGE    (0b00001111, airtouch.constant.AirConditionerControlConstants.FanSpeed.NO_CHANGE); // Anything that is not one of the above. Just using 1111 as a placeholder.

        private int bits;
        private airtouch.constant.AirConditionerControlConstants.FanSpeed generic;

        FanSpeed(int bytes, airtouch.constant.AirConditionerControlConstants.FanSpeed generic) {
            this.bits = bytes;
            this.generic = generic;
        }

        public int getBits() {
            return bits;
        }

        public airtouch.constant.AirConditionerControlConstants.FanSpeed getGeneric() {
            return generic;
        }
    }

    public enum SetpointControl implements AirtouchConstant<airtouch.constant.AirConditionerControlConstants.SetpointControl> {
        //                      ||      Bits 8 and 7 of Byte 3
        NO_CHANGE            (0b00000000, airtouch.constant.AirConditionerControlConstants.SetpointControl.NO_CHANGE),  // 00
        SET_TO_VALUE         (0b01000000, airtouch.constant.AirConditionerControlConstants.SetpointControl.SET_TO_VALUE),  // 01
        VALUE_DECREASE       (0b10000000, airtouch.constant.AirConditionerControlConstants.SetpointControl.VALUE_DECREASE),  // 10
        VALUE_INCREASE       (0b11000000, airtouch.constant.AirConditionerControlConstants.SetpointControl.VALUE_INCREASE);  // 11

        private int bits;
        private airtouch.constant.AirConditionerControlConstants.SetpointControl generic;

        SetpointControl(int bits, airtouch.constant.AirConditionerControlConstants.SetpointControl generic) {
            this.bits = bits;
            this.generic = generic;
        }

        public int getBits() {
            return bits;
        }

        public airtouch.constant.AirConditionerControlConstants.SetpointControl getGeneric() {
            return generic;
        }

    }

}
