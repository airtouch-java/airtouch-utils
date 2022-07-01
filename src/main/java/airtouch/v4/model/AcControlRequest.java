package airtouch.v4.model;

import airtouch.v4.constant.AcControlConstants.*;


public class AcControlRequest {
    
    private int acNumber;
    private AcPower acPower;
    private Mode acMode;
    private FanSpeed fanSpeed;
    private SetpointControl setpointControl;
    private int setpointValue = 0x3f;
    
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
        byte byte1 = (byte) (this.acPower.getBits() | (this.acNumber & 0b00000011));
        byte byte2 = (byte) (this.acMode.getBits() | this.fanSpeed.getBits());
        byte byte3 = (byte) (this.setpointControl.getBits() | (this.setpointValue & 0xFF));
        return new byte[] {
                byte1, byte2, byte3, (byte) 0x00};
     }
}
