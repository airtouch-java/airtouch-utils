package airtouch.v5.model;

import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.AcStatusConstants.FanSpeed;
import airtouch.v5.constant.AcStatusConstants.Mode;
import airtouch.v5.constant.AcStatusConstants.PowerState;
import airtouch.v5.constant.MessageConstants.MessageType;

public class AirConditionerStatusResponse {
    
    private MessageConstants.MessageType messageType = MessageType.AC_STATUS;
    private Integer acNumber;
    private PowerState powerstate;
    private Mode mode;
    private FanSpeed fanSpeed;
    private boolean spill;
    private boolean acTimer;
    private int targetSetpoint;
    private Integer currentTemperature;
    private int errorCode;


    
    public MessageConstants.MessageType getMessageType() {
        return messageType;
    }

    public Integer getAcNumber() {
        return acNumber;
    }
    public void setAcNumber(Integer acNumber) {
        this.acNumber = acNumber;
    }

    public PowerState getPowerstate() {
        return powerstate;
    }
    
    public void setPowerstate(PowerState powerstate) {
        this.powerstate = powerstate;
    }
    
    public Mode getMode() {
        return mode;
    }
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public FanSpeed getFanSpeed() {
        return fanSpeed;
    }
    
    public void setFanSpeed(FanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }
    
    public boolean isSpill() {
        return spill;
    }
    
    public void setSpill(boolean spill) {
        this.spill = spill;
    }
    
    public boolean isAcTimer() {
        return acTimer;
    }
    
    public void setAcTimer(boolean acTimer) {
        this.acTimer = acTimer;
    }
    
    public int getTargetSetpoint() {
        return targetSetpoint;
    }
    public void setTargetSetpoint(int targetSetpoint) {
        this.targetSetpoint = targetSetpoint;
    }
    
    public Integer getCurrentTemperature() {
        return currentTemperature;
    }
    public void setCurrentTemperature(Integer currentTemperature) {
        this.currentTemperature = currentTemperature;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "AcStatusResponse [messageType=" + messageType + ", acNumber=" + acNumber + ", powerstate=" + powerstate
                + ", mode=" + mode + ", fanSpeed=" + fanSpeed + ", spill=" + spill + ", acTimer=" + acTimer
                + ", targetSetpoint=" + targetSetpoint + ", currentTemperature=" + currentTemperature + ", errorCode="
                + errorCode + "]";
    }
    
    
}
