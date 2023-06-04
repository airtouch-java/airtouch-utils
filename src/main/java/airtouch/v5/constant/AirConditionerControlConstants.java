package airtouch.v5.constant;

public class AirConditionerControlConstants {

    public enum AcPower {
        //                      ||||     Bits 8 to 5 of byte 1
        NEXT_POWER_STATE     (0b00010000),  // 0001
        POWER_OFF            (0b00100000),  // 0010
        POWER_ON             (0b00110000),  // 0011
        SET_TO_AWAY          (0b10000000),  // 1000
        SET_TO_SLEEP         (0b10000000),  // 1001
        NO_CHANGE            (0b00000000); // Anything that is not one of the above. Just using 0000 as per docs.

        private int bits;

        AcPower(int bits) {
            this.bits = bits;
        }

        public int getBits() {
            return bits;
        }
    }

    public enum Mode {
        //              ||||    Bits 8 to 5 of byte 2
        AUTO         (0b00000000),
        HEAT         (0b00010000),
        DRY          (0b00100000),
        FAN          (0b00110000),
        COOL         (0b01000000),
        NO_CHANGE    (0b11110000); // Anything that is not one of the above. Just using 1111 as a placeholder.

        private int bits;

        Mode(int bytes) {
            this.bits = bytes;
        }

        public int getBits() {
            return bits;
        }
    }

    public enum FanSpeed {
        //                      ||||    Bits 4 to 1 of byte 2
        AUTO             (0b00000000),
        QUIET            (0b00000010),
        LOW              (0b00000010),
        MEDIUM           (0b00000011),
        HIGH             (0b00000100),
        POWERFUL         (0b00000101),
        TURBO            (0b00000110),
        INTELLIGENT_AUTO (0b00001000),
        NO_CHANGE        (0b00001111); // Anything that is not one of the above. Just using 1111 as a placeholder.

        private int bits;

        FanSpeed(int bytes) {
            this.bits = bytes;
        }

        public int getBits() {
            return bits;
        }
    }

    public enum SetpointControl {
        //                            Byte 3
        NO_CHANGE         (0x00),  // 0x00
        SET_TO_VALUE      (0x40);  // 0x40

        private int bits;

        SetpointControl(int bits) {
            this.bits = bits;
        }

        public int getBits() {
            return bits;
        }
    }

}
