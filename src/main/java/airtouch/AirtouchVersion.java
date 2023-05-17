package airtouch;

public enum AirtouchVersion {
    AIRTOUCH4 ("AirTouch4", 
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