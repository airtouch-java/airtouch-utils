package airtouch.v4.model;

import airtouch.v4.constant.GroupControlConstants.GroupControl;
import airtouch.v4.constant.GroupControlConstants.GroupPower;
import airtouch.v4.constant.GroupControlConstants.GroupSetting;

public class GroupControlRequest {

    private Integer groupNumber;
    private GroupSetting groupSetting;
    private GroupControl groupControl;
    private GroupPower groupPower;

    private int settingValue = 0;

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public GroupSetting getGroupSetting() {
        return groupSetting;
    }
    public void setGroupSetting(GroupSetting groupSetting) {
        this.groupSetting = groupSetting;
    }

    public void setGroupControl(GroupControl groupControl) {
        this.groupControl = groupControl;

    }
    public void setGroupPower(GroupPower groupPower) {
        this.groupPower = groupPower;

    }

    public void setSettingValue(int settingValue) {
        this.settingValue = settingValue;
    }

    public byte[] getBytes() {
       byte byte1 = (byte) (this.groupNumber & 0xFF);
       byte byte2 = (byte) (groupSetting.getBits() | groupControl.getBits() | groupPower.getBits());
       byte byte3 = (byte) (settingValue & 0xFF);
       return new byte[] {
               byte1, byte2, byte3, (byte) 0x00};
    }

}
