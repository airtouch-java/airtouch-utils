package airtouch.constant;

public class AirConditionerControlConstants {

    public enum AcPower {
        NEXT_POWER_STATE,	// Airtouch5
        POWER_OFF,	 		// Airtouch5
        POWER_ON,	 		// Airtouch5
        SET_TO_AWAY,		// Airtouch5
        SET_TO_SLEEP,		// Airtouch5
        NO_CHANGE,			// Airtouch5
    }

    public enum Mode {
        AUTO,	 			// Airtouch5
        HEAT,	 			// Airtouch5
        DRY,	 			// Airtouch5
        FAN,	 			// Airtouch5
        COOL,	 			// Airtouch5
        NO_CHANGE;			// Airtouch5
    }

    public enum FanSpeed {
        AUTO,	 			// Airtouch5
        QUIET,	 			// Airtouch5
        LOW,	 			// Airtouch5
        MEDIUM,	 			// Airtouch5
        HIGH,	 			// Airtouch5
        POWERFUL,	 		// Airtouch5
        TURBO,	 			// Airtouch5
        INTELLIGENT_AUTO,	// Airtouch5
        NO_CHANGE;	 		// Airtouch5
    }

    public enum SetpointControl {
        NO_CHANGE,	 		// Airtouch4, Airtouch5
        SET_TO_VALUE,	 	// Airtouch4, Airtouch5
        VALUE_DECREASE,		// Airtouch4
        VALUE_INCREASE;		// Airtouch4
    }

}
