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
     * The Response contains the following:
     * <ul>
     * <li> Mac Address
     * <li> Host IP Address
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
     *
     */
    public interface AirtouchDiscoveryBroadcastResponse {
        String getMacAddress();
        String getHostAddress();
        Integer getPortNumber();
        AirtouchVersion getAirtouchVersion();
        String getAirtouchId();
    }

}
