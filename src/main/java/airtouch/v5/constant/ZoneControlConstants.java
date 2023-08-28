package airtouch.v5.constant;

public class ZoneControlConstants {
    
    public enum ZoneSetting {
        //                    ||| Bits 8 to 6
        NO_CHANGE          (0b00000000), // 000
        VALUE_DECREASE     (0b01000000), // 010
        VALUE_INCREASE     (0b01100000), // 011
        SET_OPEN_PERCENTAGE(0b10000000), // 100
        SET_TARGET_SETPOINT(0b10100000); // 101
        
        private int bits;

        ZoneSetting(int bits) {
            this.bits = bits;
        }
        
        public int getBits() {
            return bits;
        }
    }
    
    public enum ZonePower {
        //                           |||     Bits 3 to 1
        NO_CHANGE            (0b00000000),  // 000
        NEXT_POWER_STATE     (0b00000001),  // 001
        POWER_OFF            (0b00000010),  // 010
        POWER_ON             (0b00000011),  // 011
        TURBO_POWER          (0b00000101);  // 101
        
        private int bits;
        
        ZonePower(int bits) {
            this.bits = bits;
        }
        
        public int getBits() {
            return bits;
        }
    }
    
    

}
