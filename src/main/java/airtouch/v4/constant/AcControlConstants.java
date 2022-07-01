package airtouch.v4.constant;

public class AcControlConstants {
    
    public enum AcPower {
        //                      ||     Bits 8 to 7
        NO_CHANGE            (0b00000000),  // 00
        NEXT_POWER_STATE     (0b01000000),  // 01
        POWER_OFF            (0b10000000),  // 10
        POWER_ON             (0b11000000);  // 11
        
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
        //                  ||||    Bits 4 to 1 of byte 2
        AUTO         (0b00000000),
        QUIET        (0b00000010),
        LOW          (0b00000010),
        MEDIUM       (0b00000011),
        HIGH         (0b00000100),
        POWERFUL     (0b00000101),
        TURBO        (0b00000110),
        NO_CHANGE    (0b00001111); // Anything that is not one of the above. Just using 1111 as a placeholder.
        
        private int bits;
        
        FanSpeed(int bytes) {
            this.bits = bytes;
        }
        
        public int getBits() {
            return bits;
        }
    }

    
    public enum GroupSetting {
        //                    ||| Bits 8 to 6
        NO_CHANGE          (0b00000000), // 000
        VALUE_INCREASE     (0b01000000), // 010
        VALUE_DECREASE     (0b01100000), // 011
        SET_OPEN_PERCENTAGE(0b10000000), // 100
        SET_TARGET_SETPOINT(0b10100000); // 101
        
        private int bits;

        GroupSetting(int bits) {
            this.bits = bits;
        }
        
        public int getBits() {
            return bits;
        }
    }
    
    public enum SetpointControl {
        //                      ||      Bits 8 and 7 of Byte 3
        NO_CHANGE            (0b00000000),  // 00
        SET_TO_VALUE         (0b01000000),  // 01
        VALUE_DECREASE       (0b10000000),  // 10
        VALUE_INCREASE       (0b11000000);  // 11
        
        private int bits;
        
        SetpointControl(int bits) {
            this.bits = bits;
        }
        
        public int getBits() {
            return bits;
        }
    }
    

    
    

}
