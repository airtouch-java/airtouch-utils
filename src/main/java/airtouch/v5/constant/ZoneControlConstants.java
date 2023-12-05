package airtouch.v5.constant;

import java.util.Arrays;

import airtouch.constant.AirtouchConstant;

public class ZoneControlConstants {
    
    public enum ZoneSetting implements AirtouchConstant<airtouch.constant.ZoneControlConstants.ZoneSetting> {
        //                    ||| Bits 8 to 6
        NO_CHANGE          (0b00000000, airtouch.constant.ZoneControlConstants.ZoneSetting.NO_CHANGE), // 000
        VALUE_DECREASE     (0b01000000, airtouch.constant.ZoneControlConstants.ZoneSetting.VALUE_DECREASE), // 010
        VALUE_INCREASE     (0b01100000, airtouch.constant.ZoneControlConstants.ZoneSetting.VALUE_INCREASE), // 011
        SET_OPEN_PERCENTAGE(0b10000000, airtouch.constant.ZoneControlConstants.ZoneSetting.SET_OPEN_PERCENTAGE), // 100
        SET_TARGET_SETPOINT(0b10100000, airtouch.constant.ZoneControlConstants.ZoneSetting.SET_TARGET_SETPOINT); // 101
        
        private int bits;
        private airtouch.constant.ZoneControlConstants.ZoneSetting generic;

        ZoneSetting(int bits, airtouch.constant.ZoneControlConstants.ZoneSetting generic) {
            this.bits = bits;
            this.generic = generic;
        }
        
        public int getBits() {
            return bits;
        }

        @Override
        public airtouch.constant.ZoneControlConstants.ZoneSetting getGeneric() {
            return this.generic;
        }
        
        public static ZoneSetting getSpecific(airtouch.constant.ZoneControlConstants.ZoneSetting generic) {
            return Arrays.stream(values())
                    .filter(s -> s.getGeneric().name().equals(generic.name()))
                    .findFirst()
                    .orElse(NO_CHANGE);
        }
    }
    public enum ZoneControl implements AirtouchConstant<airtouch.constant.ZoneControlConstants.ZoneControl>{
        //                         ||      Bits 5 and 4
        NO_CHANGE            (0b00000000, airtouch.constant.ZoneControlConstants.ZoneControl.NO_CHANGE),  // 00
        TOGGLE_CONTROL_METHOD(0b00001000, airtouch.constant.ZoneControlConstants.ZoneControl.TOGGLE_CONTROL_METHOD),  // 01
        PERCENTAGE_CONTROL   (0b00010000, airtouch.constant.ZoneControlConstants.ZoneControl.PERCENTAGE_CONTROL),  // 10
        TEMPERATURE_CONTROL  (0b00011000, airtouch.constant.ZoneControlConstants.ZoneControl.TEMPERATURE_CONTROL);  // 11
        
        private int bits;
        private airtouch.constant.ZoneControlConstants.ZoneControl generic;
        
        ZoneControl(int bits, airtouch.constant.ZoneControlConstants.ZoneControl generic) {
            this.bits = bits;
            this.generic = generic;
        }
        
        public int getBits() {
            return bits;
        }
        
        @Override
        public airtouch.constant.ZoneControlConstants.ZoneControl getGeneric() {
            return generic;
        }
        
        public static ZoneControl getSpecific(airtouch.constant.ZoneControlConstants.ZoneControl generic) {
            return Arrays.stream(values())
                    .filter(s -> s.getGeneric().name().equals(generic.name()))
                    .findFirst()
                    .orElse(NO_CHANGE);
        }
    }
            
    public enum ZonePower implements AirtouchConstant<airtouch.constant.ZoneControlConstants.ZonePower> {
        //                           |||     Bits 3 to 1
        NO_CHANGE            (0b00000000, airtouch.constant.ZoneControlConstants.ZonePower.NO_CHANGE),  // 000
        NEXT_POWER_STATE     (0b00000001, airtouch.constant.ZoneControlConstants.ZonePower.NEXT_POWER_STATE),  // 001
        POWER_OFF            (0b00000010, airtouch.constant.ZoneControlConstants.ZonePower.POWER_OFF),  // 010
        POWER_ON             (0b00000011, airtouch.constant.ZoneControlConstants.ZonePower.POWER_ON),  // 011
        TURBO_POWER          (0b00000101, airtouch.constant.ZoneControlConstants.ZonePower.TURBO_POWER);  // 101
        
        private int bits;
        private airtouch.constant.ZoneControlConstants.ZonePower generic;
        
        ZonePower(int bits, airtouch.constant.ZoneControlConstants.ZonePower generic) {
            this.bits = bits;
            this.generic = generic;
        }
        
        public int getBits() {
            return bits;
        }
        
        @Override
        public airtouch.constant.ZoneControlConstants.ZonePower getGeneric() {
            return this.generic;
        }
        public static ZonePower getSpecific(airtouch.constant.ZoneControlConstants.ZonePower generic) {
            return Arrays.stream(values())
                    .filter(s -> s.getGeneric().name().equals(generic.name()))
                    .findFirst()
                    .orElse(NO_CHANGE);
        }
    }
    
    

}
