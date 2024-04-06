package airtouch.discovery;

import airtouch.AirtouchVersion;
/**
 * Callback invoked when a valid response is received from a UDP broadcast reply when searching for an AirTouch on the network.
 * See {@link #handleResponse(AirtouchDiscoveryBroadcastResponse)} for details
 */
public interface AirtouchDiscoveryBroadcastResponseCallback {

    /**
     * Callback invoked when a valid response is received from a UDP broadcast reply when searching for an AirTouch on the network.
     *
     * The Response contains the following for AirTouch4:
     * <ul>
     * <li> Mac Address
     * <li> Host IP Address
     * <li> Listening Port (TCP)
     * <li> {@link AirtouchVersion}
     * <li> AirTouch Unique ID
     * </ul>
     * or the following for AirTouch5
     * <ul>
     * <li> Host IP Address
     * <li> Console ID
     * <li> Listening Port (TCP)
     * <li> {@link AirtouchVersion}
     * <li> AirTouch Unique ID
     * </ul>
     *
     * @param response object containing the above values obtained from the AirTouch broadcast message.
     */
    void handleResponse(AirtouchDiscoveryBroadcastResponse response);

    /**
     * {@link AirtouchDiscoveryBroadcastResponse} object containing details extraced from a UDP broadcast searching for an AirTouch.<p>
     * Contains the following:
     * <ul>
     * <li> Mac Address
     * <li> Host IP Address
     * <li> Listening Port (TCP)
     * <li> {@link AirtouchVersion}
     * <li> AirTouch Unique ID
     * </ul>
     * or the following or AirTouch5
     * <ul>
     * <li> Host IP Address
     * <li> Console ID
     * <li> Listening Port (TCP)
     * <li> {@link AirtouchVersion}
     * <li> AirTouch Unique ID
     * </ul>
     */
    public interface AirtouchDiscoveryBroadcastResponse {
        /** Only for AirTouch4 
         * @return the MAC address from the broadcast response
         */
        String getMacAddress();
        /** Only for AirTouch5 
         * @return Unique ConsoleId for this Airtouch Hardware
         */
        String getConsoleId();
        /**
         * @return IP address of the Airtouch Hardware on the network
         */
        String getHostAddress();
        /**
         * Listening port of Airtouch.
         * @return Port number that the Airtouch is listening on for TCP connections
         */
        Integer getPortNumber();
        /**
         * Airtouch version as reported by Airtouch, eg "Airtouch5"
         * @return Airtouch Version 
         */
        AirtouchVersion getAirtouchVersion();
        /**
         * Get the AirTouchId. Not sure of the difference from ConsoleId (Airtouch5).
         * @return ID to identify Airtouch
         */
        String getAirtouchId();
        /** Only for AirTouch5 
         * Device name as defined in Airtouch5 system
         * @return Device Name.
         * */
        String getDeviceName();
    }

}
