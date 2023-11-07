package airtouch.v4.constant;

import java.util.Arrays;

import airtouch.constant.AirtouchConstant;
import airtouch.constant.ZoneControlConstants.ZoneControl;
import airtouch.constant.ZoneControlConstants.ZonePower;
import airtouch.constant.ZoneControlConstants.ZoneSetting;

public class GroupControlConstants {
    
    public enum GroupSetting implements AirtouchConstant<ZoneSetting>{
        //                    ||| Bits 8 to 6
        NO_CHANGE          (0b00000000, ZoneSetting.NO_CHANGE), // 000
        VALUE_DECREASE     (0b01000000, ZoneSetting.VALUE_DECREASE), // 010
        VALUE_INCREASE     (0b01100000, ZoneSetting.VALUE_INCREASE), // 011
        SET_OPEN_PERCENTAGE(0b10000000, ZoneSetting.SET_OPEN_PERCENTAGE), // 100
        SET_TARGET_SETPOINT(0b10100000, ZoneSetting.SET_TARGET_SETPOINT); // 101
        
        private int bits;
        private ZoneSetting generic;

        GroupSetting(int bits, ZoneSetting generic) {
            this.bits = bits;
            this.generic = generic;
        }
        
        public int getBits() {
            return bits;
        }
        @Override
        public ZoneSetting getGeneric() {
            return this.generic;
        }
        
        public static GroupSetting getSpecific(ZoneSetting generic) {
            return Arrays.stream(values())
                    .filter(s -> s.getGeneric().name().equals(generic.name()))
                    .findFirst()
                    .orElse(NO_CHANGE);
        }
    }
    
    public enum GroupControl implements AirtouchConstant<ZoneControl>{
        //                         ||      Bits 5 and 4
        NO_CHANGE            (0b00000000, ZoneControl.NO_CHANGE),  // 00
        TOGGLE_CONTROL_METHOD(0b00001000, ZoneControl.TOGGLE_CONTROL_METHOD),  // 01
        PERCENTAGE_CONTROL   (0b00010000, ZoneControl.PERCENTAGE_CONTROL),  // 10
        TEMPERATURE_CONTROL  (0b00011000, ZoneControl.TEMPERATURE_CONTROL);  // 11
        
        private int bits;
        private ZoneControl generic;
        
        GroupControl(int bits, ZoneControl generic) {
            this.bits = bits;
            this.generic = generic;
        }
        
        public int getBits() {
            return bits;
        }
        
        @Override
        public ZoneControl getGeneric() {
            return this.generic;
        }
        
        public static GroupControl getSpecific(ZoneControl generic) {
            return Arrays.stream(values())
                    .filter(s -> s.getGeneric().name().equals(generic.name()))
                    .findFirst()
                    .orElse(NO_CHANGE);
        }
    }
    
    public enum GroupPower implements AirtouchConstant<ZonePower> {
        //                                                                  |||     Bits 3 to 1
        NO_CHANGE            (0b00000000, ZonePower.NO_CHANGE),          // 000
        NEXT_POWER_STATE     (0b00000001, ZonePower.NEXT_POWER_STATE),   // 001
        POWER_OFF            (0b00000010, ZonePower.POWER_OFF),          // 010
        POWER_ON             (0b00000011, ZonePower.POWER_ON),           // 011
        TURBO_POWER          (0b00000101, ZonePower.TURBO_POWER);        // 101
        
        private int bits;
        private ZonePower generic;
        
        GroupPower(int bits, ZonePower generic) {
            this.bits = bits;
            this.generic = generic;
        }
        
        public int getBits() {
            return bits;
        }

        @Override
        public ZonePower getGeneric() {
            return this.generic;
        }

        public static GroupPower getSpecific(ZonePower generic) {
            return Arrays.stream(values())
                    .filter(s -> s.getGeneric().name().equals(generic.name()))
                    .findFirst()
                    .orElse(NO_CHANGE);
        }
    }
    
    

}
