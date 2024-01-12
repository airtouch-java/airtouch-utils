package airtouch.v4.builder;

import airtouch.Request;
import airtouch.constant.ZoneControlConstants.ZoneControl;
import airtouch.constant.ZoneControlConstants.ZonePower;
import airtouch.constant.ZoneControlConstants.ZoneSetting;
import airtouch.v4.constant.GroupControlConstants.GroupControl;
import airtouch.v4.constant.GroupControlConstants.GroupPower;
import airtouch.v4.constant.GroupControlConstants.GroupSetting;
import airtouch.v4.constant.MessageConstants;
import airtouch.v4.handler.GroupControlHandler;
import airtouch.v4.model.GroupControlRequest;

public class GroupControlRequestBuilder {

    private Integer zoneNumber;
    private ZoneSetting zoneSetting;
    private ZoneControl zoneControl;
    private ZonePower zonePower;

    private Integer settingValue;

    /**
     * {@link GroupControlRequestBuilder} to create a {@link GroupControlRequest}.
     *
     * @param zoneNumber - Zone number for this control message. Zero based.
     */
    public GroupControlRequestBuilder(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    /**
     * Method to set the {@link ZoneSetting}.<br>
     * Calling this method is optional. If not called, the request will default to ZoneSetting.NO_CHANGE.
     * If called, groupSetting must be one of:<ul>
     * <li>NO_CHANGE - No change made to group setting
     * <li>VALUE_DECREASE - Change value by -1°C (for set-point) or -5% (for open percentage)
     * <li>VALUE_INCREASE - Change value by +1°C (for set-point) or +5% (for open percentage)
     * <li>SET_OPEN_PERCENTAGE - Set open percentage. Also call {@link #settingValue(int)} to specify new value.
     * <li>SET_TARGET_SETPOINT - Set target set-point. Also call {@link #settingValue(int)} to specify new value.
     * </ul>
     *
     * @param groupSetting - {@link ZoneSetting} enum value
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder setting(ZoneSetting groupSetting) {
        this.zoneSetting = groupSetting;
        return this;
    }

    /**
     * Method to set the {@link ZoneControl}.<br>
     * Calling this method is optional. If not called, the request will default to ZoneControl.NO_CHANGE.
     * If called, groupControl must be one of:<ul>
     * <li>NO_CHANGE - No change made to group control
     * <li>TOGGLE_CONTROL_METHOD - Change from % open to °C or vice-versa
     * <li>PERCENTAGE_CONTROL - Set to use percentage open control for this group
     * <li>TEMPERATURE_CONTROL - Set to use temperature control for this group
     * </ul>
     * @param zoneControl
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder control(ZoneControl zoneControl) {
        this.zoneControl = zoneControl;
        return this;
    }

    /**
     * Method to set the {@link ZonePower}.<br>
     * Calling this method is optional. If not called, the request will default to ZonePower.NO_CHANGE.
     * If called, zonePower must be one of:<ul>
     * <li>NO_CHANGE - No change made to group power
     * <li>NEXT_POWER_STATE - Change to next power state
     * <li>POWER_OFF - Turn off group
     * <li>POWER_ON - Turn on group
     * <li>TURBO_POWER - Turn group to Turbo
     * </ul>
     * @param zonePower
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder power(ZonePower zonePower) {
        this.zonePower = zonePower;
        return this;
    }

    /**
     * Method to set the numeric value when called in conjunction with {@link #setting(ZoneSetting)}.
     * When calling  {@link #setting(ZoneSetting)}, this method MUST be called to specify the value
     * associated with the {@link ZoneSetting}.
     *
     * @param settingValue - numeric value that matches the {@link ZoneSetting} specified by {@link #setting(ZoneSetting)}.
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder settingValue(int settingValue) {
        this.settingValue = settingValue;
        return this;
    }

    public GroupControlRequest build() {
        GroupControlRequest request = new GroupControlRequest();
        request.setGroupNumber(this.zoneNumber);

        if (this.zoneSetting == null) {
            request.setGroupSetting(GroupSetting.NO_CHANGE);
        } else if (GroupSetting.SET_OPEN_PERCENTAGE.equals(GroupSetting.getSpecific(this.zoneSetting))
                || GroupSetting.SET_TARGET_SETPOINT.equals(GroupSetting.getSpecific(this.zoneSetting))){
            if (this.settingValue == null) {
                throw new IllegalArgumentException(
                        String.format("setting value must be defined when GroupSettings is %s", this.zoneSetting));
            }
            request.setGroupSetting(GroupSetting.getSpecific(this.zoneSetting));
            request.setSettingValue(this.settingValue);
        } else {
            request.setGroupSetting(GroupSetting.getSpecific(this.zoneSetting));
            request.setSettingValue(0);
        }

        if (this.zoneControl == null) {
            request.setGroupControl(GroupControl.NO_CHANGE);
        } else {
            request.setGroupControl(GroupControl.getSpecific(this.zoneControl));
        }

        if (this.zonePower == null) {
            request.setGroupPower(GroupPower.NO_CHANGE);
        } else {
            request.setGroupPower(GroupPower.getSpecific(this.zonePower));
        }

        return request;
    }

    public Request<MessageConstants.Address> build(int messageId) {
        return GroupControlHandler.generateRequest(messageId, build());
    }
}
