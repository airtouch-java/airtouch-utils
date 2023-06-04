package airtouch.v5.model;

import airtouch.v5.constant.AirConditionerControlConstants.AcPower;
import airtouch.v5.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.v5.constant.AirConditionerControlConstants.Mode;
import airtouch.v5.constant.AirConditionerControlConstants.SetpointControl;


public class AirConditionerControlRequest {

    private int acNumber;
    private AcPower acPower;
    private Mode acMode;
    private FanSpeed fanSpeed;
    private SetpointControl setpointControl;
    private int setpointValue = 0xff; // Default 0xFF in accordance with examples from docs.

    public void setAcNumber(int acNumber) {
        this.acNumber = acNumber;
    }
    public void setAcPower(AcPower acPower) {
        this.acPower = acPower;
    }
    public void setAcMode(Mode acMode) {
        this.acMode = acMode;
    }
    public void setFanSpeed(FanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }
    public void setSetpointControl(SetpointControl setpointControl) {
        this.setpointControl = setpointControl;
    }
    public void setSetpointValue(Integer setpointValue) {
        this.setpointValue = setpointValue;
    }

    public byte[] getBytes() {
        
        byte byte1 = (byte) (this.acPower.getBits() | (this.acNumber & 0b00001111));
        byte byte2 = (byte) (this.acMode.getBits() | this.fanSpeed.getBits());
        byte byte3 = (byte) this.setpointControl.getBits();
        byte byte4 = (byte) (this.setpointValue & 0xFF);
        
        return new byte[] { byte1, byte2, byte3, byte4 };
     }
}
