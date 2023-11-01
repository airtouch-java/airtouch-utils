package airtouch.model;

import airtouch.MessageType;

public class ZoneNameResponse {

    private MessageType messageType = MessageType.ZONE_NAME;
    private int zoneNumber;
    private String name;

    public MessageType getMessageType() {
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
        return "ZoneName [messageType=" + messageType + ", zoneNumber=" + zoneNumber + ", name=" + name + "]";
    }


}
