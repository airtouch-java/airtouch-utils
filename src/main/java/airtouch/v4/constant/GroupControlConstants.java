package airtouch.v4.constant;

public class GroupControlConstants {
    
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
    
    public enum GroupControl {
        //                         ||      Bits 5 and 4
        NO_CHANGE            (0b00000000),  // 00
        TOGGLE_CONTROL_METHOD(0b00001000),  // 01
        PERCENTAGE_CONTROL   (0b00010000),  // 10
        TEMPERATURE_CONTROL  (0b00011000);  // 11
        
        private int bits;
        
        GroupControl(int bits) {
            this.bits = bits;
        }
        
        public int getBits() {
            return bits;
        }
    }
    
    public enum GroupPower {
        //                           |||     Bits 3 to 1
        NO_CHANGE            (0b00000000),  // 000
        NEXT_POWER_STATE     (0b00000001),  // 001
        POWER_OFF            (0b00000010),  // 010
        POWER_ON             (0b00000011),  // 011
        TURBO_POWER          (0b00000101);  // 101
        
        private int bits;
        
        GroupPower(int bits) {
            this.bits = bits;
        }
        
        public int getBits() {
            return bits;
        }
    }
    
    

}
