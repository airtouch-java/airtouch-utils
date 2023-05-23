package airtouch;

/**
 * Enum representing the AirTouch versions supported by this library.
 *
 * Contains reference values for the version.
 * <ul>
 * <li>versionIdentifier - eg, AirTouch4
 * <li>listeningPort - eg, 9004
 * <li>discoveryPort - eg, 49004
 * <li>discoveryMessage - Message to broadcast on network to trigger a response from the AirTouch
 * </ul>
 */
public enum AirtouchVersion {
    AIRTOUCH4 (
            airtouch.v4.constant.ConnectionConstants.AIRTOUCH_VERSION_IDENTIFIER,
            airtouch.v4.constant.ConnectionConstants.AIRTOUCH_LISTEN_PORT,
            airtouch.v4.constant.ConnectionConstants.DISCOVERY_LISTEN_PORT,
            airtouch.v4.constant.ConnectionConstants.DISCOVERY_BROADCAST_MESSAGE),
    AIRTOUCH5 ("AirTouch5",
            9005,
            49005,
            "::REQUEST-POLYAIRE-AIRTOUCH-DEVICE-INFO:;");

    private String versionIdentifier;
    private int listeningPort;
    private int discoveryPort;

    private String discoveryMessage;

    AirtouchVersion(String versionIdentifier, int listeningPort, int discoveryPort, String discoveryMessage) {
        this.versionIdentifier = versionIdentifier;
        this.listeningPort = listeningPort;
        this.discoveryPort = discoveryPort;
        this.discoveryMessage = discoveryMessage;
    }

    /**
     * version string, as returned by the Airtouch Broadcast Response.
     */
    public String getVersionIdentifier() {
        return versionIdentifier;
    }

    /**
     * TCP Port number the Airtouch console listens on.
     * Used to connect to the airtouch when interacting with it under normal operation.
     */
    public int getListeningPort() {
        return listeningPort;
    }

    /**
     * UDP Port number on which to send and listen for discovery broadcasts.
     */
    public int getDiscoveryPort() {
        return discoveryPort;
    }
    /**
     * The message to UDP broadcast on the discoveryPort so that Airtouch responds with its information.
     */
    public String getDiscoveryMessage() {
        return discoveryMessage;
    }
}
