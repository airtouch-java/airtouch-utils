package airtouch.v5.constant;

public class ConnectionConstants {
    private ConnectionConstants() {} // Prevent instantiation.
    public static final int DISCOVERY_LISTEN_PORT = 49005;
    public static final int AIRTOUCH_LISTEN_PORT = 9005;
    public static final String DISCOVERY_BROADCAST_MESSAGE = "::REQUEST-POLYAIRE-AIRTOUCH-DEVICE-INFO:;";
    public static final String AIRTOUCH_VERSION_IDENTIFIER = "AirTouch5";

}
