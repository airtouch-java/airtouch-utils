package airtouch.constant;

public class AirConditionerStatusConstants {

    public enum PowerState {
        OFF,
        ON,
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
