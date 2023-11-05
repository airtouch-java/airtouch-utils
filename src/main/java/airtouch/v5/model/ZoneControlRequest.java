package airtouch.v5.model;

import airtouch.model.ControlRequest;
import airtouch.v5.constant.ZoneControlConstants.ZoneControl;
import airtouch.v5.constant.ZoneControlConstants.ZonePower;
import airtouch.v5.constant.ZoneControlConstants.ZoneSetting;

public class ZoneControlRequest implements ControlRequest {

    private Integer zoneNumber;
    private ZoneSetting zoneSetting;
    private ZoneControl zoneControl;
    private ZonePower zonePower;

    private int settingValue = 0;

    public void setZoneNumber(Integer zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public ZoneSetting getZoneSetting() {
        return zoneSetting;
    }
    public void setZoneSetting(ZoneSetting zoneSetting) {
        this.zoneSetting = zoneSetting;
    }

    public void setZoneControl(ZoneControl zoneControl) {
        this.zoneControl = zoneControl;
    }

    public void setZonePower(ZonePower zonePower) {
        this.zonePower = zonePower;
    }

    public void setSettingValue(int settingValue) {
        this.settingValue = settingValue;
    }

    public byte[] getBytes() {
       byte byte1 = (byte) (this.zoneNumber & 0b00111111);
       byte byte2 = (byte) (zoneSetting.getBits() | zoneControl.getBits() | zonePower.getBits());
       byte byte3 = (byte) (settingValue & 0xFF);
       return new byte[] {
               byte1, byte2, byte3, (byte) 0x00};
    }

}
