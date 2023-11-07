package airtouch.constant;

public class ZoneControlConstants {
    
    public enum ZoneSetting {
        NO_CHANGE,
        VALUE_DECREASE,
        VALUE_INCREASE,
        SET_OPEN_PERCENTAGE,
        SET_TARGET_SETPOINT;
        
    }
    
    public enum ZoneControl{
        NO_CHANGE,
        TOGGLE_CONTROL_METHOD,
        PERCENTAGE_CONTROL,
        TEMPERATURE_CONTROL;
    }
    
    public enum ZonePower {
        NO_CHANGE,
        NEXT_POWER_STATE,
        POWER_OFF,
        POWER_ON,
        TURBO_POWER;
    }

}
