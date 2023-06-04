package airtouch.v5.model;

import airtouch.v5.constant.MessageConstants.ExtendedMessageType;

public class ZoneNameResponse {
    
    private ExtendedMessageType messageType = ExtendedMessageType.ZONE_NAME;
    private int zoneNumber;
    private String name;
    
    public ExtendedMessageType getMessageType() {
        return messageType;
    }
    
    public int getZoneNumber() {
        return zoneNumber;
    }
    
    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ZoneName [messageType=" + messageType + ", groupNumber=" + zoneNumber + ", name=" + name + "]";
    }
    
    
}
