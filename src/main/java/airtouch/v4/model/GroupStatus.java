package airtouch.v4.model;

import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.GroupStatusConstants.*;
import airtouch.v4.constant.MessageConstants.MessageType;

public class GroupStatus {

    private MessageConstants.MessageType messageType = MessageType.GROUP_CONTROL;
    private PowerState powerstate;
    private int groupNumber;
    private ControlMethod controlMethod;
    private int openPercentage;
    private boolean batteryLow;
    private boolean turboSupported;
    private int targetSetpoint;
    private boolean hasSensor;
    private Integer currentTemperature;
    private boolean spill;

    public MessageConstants.MessageType getMessageType() {
        return messageType;
    }
    public PowerState getPowerstate() {
        return powerstate;
    }
    public void setPowerstate(PowerState powerstate) {
        this.powerstate = powerstate;
    }
    
    public int getGroupNumber() {
        return groupNumber;
    }
    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
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
    
    public Integer getCurrentTemperature() {
        return currentTemperature;
    }
    public void setCurrentTemperature(Integer currentTemperature) {
        this.currentTemperature = currentTemperature;
    }
    
    public boolean isSpill() {
        return spill;
    }
    public void setSpill(boolean spill) {
        this.spill = spill;
    }

}
