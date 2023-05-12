package airtouch.v4.discovery;

import airtouch.AirtouchVersion;

public interface BroadcastResponseCallback {
    void handleResponse(BroadcastResponse response);
    
    public interface BroadcastResponse {
        String getMacAddress();
        String getHostAddress();
        Integer getPortNumber();
        AirtouchVersion getAirtouchVersion();
        String getAirtouchId();
    }
    
}
