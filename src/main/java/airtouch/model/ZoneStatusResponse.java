package airtouch.model;

import airtouch.constant.ZoneStatusConstants.*;
import airtouch.MessageType;

public class ZoneStatusResponse {

    private MessageType messageType = MessageType.ZONE_STATUS;
    private PowerState powerstate;
    private int zoneNumber;
    private ControlMethod controlMethod;
    private int openPercentage;
    private boolean batteryLow;
    private boolean turboSupported;
    private int targetSetpoint;
    private boolean hasSensor;
    private Double currentTemperature;
    private boolean spill;

    public MessageType getMessageType() {
        return messageType;
    }
    public PowerState getPowerstate() {
        return powerstate;
    }
    public void setPowerstate(PowerState powerstate) {
        this.powerstate = powerstate;
    }

    public int getZoneNumber() {
        return zoneNumber;
    }
    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public ControlMethod getControlMethod() {
        return controlMethod;
    }
    public void setControlMethod(ControlMethod controlMethod) {
        this.controlMethod = controlMethod;
    }

    public int getOpenPercentage() {
        return openPercentage;
    }
    public void setOpenPercentage(int openPercentage) {
        this.openPercentage = openPercentage;
    }

    public Boolean isBatteryLow() {
        return batteryLow;
    }
    public void setBatteryLow(boolean batteryLow) {
        this.batteryLow = batteryLow;
    }

    public boolean isTurboSupported() {
        return turboSupported;
    }
    public void setTurboSupported(boolean turboSupported) {
        this.turboSupported = turboSupported;
    }

    public int getTargetSetpoint() {
        return targetSetpoint;
    }
    public void setTargetSetpoint(int targetSetpoint) {
        this.targetSetpoint = targetSetpoint;
    }

    public boolean hasSensor() {
        return hasSensor;
    }
    public void setHasTemperatureSensor(boolean hasSensor) {
        this.hasSensor = hasSensor;
    }

    public Double getCurrentTemperature() {
        return currentTemperature;
    }
    public void setCurrentTemperature(Double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public boolean isSpill() {
        return spill;
    }
    public void setSpill(boolean spill) {
        this.spill = spill;
    }
    @Override
    public String toString() {
        return "ZoneStatus [messageType=" + messageType + ", powerstate=" + powerstate + ", zoneNumber=" + zoneNumber
                + ", controlMethod=" + controlMethod + ", openPercentage=" + openPercentage + ", batteryLow="
                + batteryLow + ", turboSupported=" + turboSupported + ", targetSetpoint=" + targetSetpoint
                + ", hasSensor=" + hasSensor + ", currentTemperature=" + currentTemperature + ", spill=" + spill + "]";
    }


}
