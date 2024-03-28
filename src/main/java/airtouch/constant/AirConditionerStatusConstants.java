package airtouch.constant;

public class AirConditionerStatusConstants {

    public enum PowerState {
        OFF,
        ON,
        /**Airtouch5 only*/
        AWAY_OFF,
        /**Airtouch5 only*/
        AWAY_ON,
        /**Airtouch5 only*/
        SLEEP,
        NOT_AVAILABLE;
    }

    public enum Mode {
        AUTO,
        HEAT,
        DRY,
        FAN,
        COOL,
        AUTO_HEAT,
        AUTO_COOL,
        NOT_AVAILABLE;
    }
    public enum FanSpeed {
        AUTO,
        QUIET,
        LOW,
        MEDIUM,
        HIGH,
        POWERFUL,
        TURBO,
        NOT_AVAILABLE;
    }
}
