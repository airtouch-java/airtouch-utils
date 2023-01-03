package airtouch.v4.builder;

import airtouch.v4.Request;
import airtouch.v4.constant.GroupControlConstants.GroupControl;
import airtouch.v4.constant.GroupControlConstants.GroupPower;
import airtouch.v4.constant.GroupControlConstants.GroupSetting;
import airtouch.v4.handler.GroupControlHandler;
import airtouch.v4.model.GroupControlRequest;

public class GroupControlRequestBuilder {

    private Integer groupNumber;
    private GroupSetting groupSetting;
    private GroupControl groupControl;
    private GroupPower groupPower;

    private Integer settingValue;

    /**
     * {@link GroupControlRequestBuilder} to create a {@link GroupControlRequest}.
     *
     * @param groupNumber - Group number for this control message. Zero based.
     */
    public GroupControlRequestBuilder(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    /**
     * Method to set the {@link GroupSetting}.<br>
     * Calling this method is optional. If not called, the request will default to GroupSetting.NO_CHANGE.
     * If called, groupSetting must be one of:<ul>
     * <li>NO_CHANGE - No change made to group setting
     * <li>VALUE_DECREASE - Change value by -1°C (for set-point) or -5% (for open percentage)
     * <li>VALUE_INCREASE - Change value by +1°C (for set-point) or +5% (for open percentage)
     * <li>SET_OPEN_PERCENTAGE - Set open percentage. Also call {@link #settingValue(int)} to specify new value.
     * <li>SET_TARGET_SETPOINT - Set target set-point. Also call {@link #settingValue(int)} to specify new value.
     * </ul>
     *
     * @param groupSetting - {@link GroupSetting} enum value
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder setting(GroupSetting groupSetting) {
        this.groupSetting = groupSetting;
        return this;
    }

    /**
     * Method to set the {@link GroupControl}.<br>
     * Calling this method is optional. If not called, the request will default to GroupControl.NO_CHANGE.
     * If called, groupControl must be one of:<ul>
     * <li>NO_CHANGE - No change made to group control
     * <li>TOGGLE_CONTROL_METHOD - Change from % open to °C or vice-versa
     * <li>PERCENTAGE_CONTROL - Set to use percentage open control for this group
     * <li>TEMPERATURE_CONTROL - Set to use temperature control for this group
     * </ul>
     * @param groupControl
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder control(GroupControl groupControl) {
        this.groupControl = groupControl;
        return this;
    }

    /**
     * Method to set the {@link GroupPower}.<br>
     * Calling this method is optional. If not called, the request will default to GroupPower.NO_CHANGE.
     * If called, groupPower must be one of:<ul>
     * <li>NO_CHANGE - No change made to group power
     * <li>NEXT_POWER_STATE - Change to next power state
     * <li>POWER_OFF - Turn off group
     * <li>POWER_ON - Turn on group
     * <li>TURBO_POWER - Turn group to Turbo
     * </ul>
     * @param groupPower
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder power(GroupPower groupPower) {
        this.groupPower = groupPower;
        return this;
    }

    /**
     * Method to set the numeric value when called in conjunction with {@link #setting(GroupSetting)}.
     * When calling  {@link #setting(GroupSetting)}, this method MUST be called to specify the value
     * associated with the {@link GroupSetting}.
     *
     * @param settingValue - numeric value that matches the {@link GroupSetting} specified by {@link #setting(GroupSetting)}.
     * @return {@link GroupControlRequestBuilder} to support fluent builder pattern.
     */
    public GroupControlRequestBuilder settingValue(int settingValue) {
        this.settingValue = settingValue;
        return this;
    }

    public GroupControlRequest build() {
        GroupControlRequest request = new GroupControlRequest();
        request.setGroupNumber(this.groupNumber);

        if (this.groupSetting == null) {
            request.setGroupSetting(GroupSetting.NO_CHANGE);
        } else if (GroupSetting.SET_OPEN_PERCENTAGE.equals(this.groupSetting)
                || GroupSetting.SET_TARGET_SETPOINT.equals(this.groupSetting)){
            if (this.settingValue == null) {
                throw new IllegalArgumentException(
                        String.format("setting value must be defined when GroupSettings is %s", this.groupSetting));
            }
            request.setGroupSetting(this.groupSetting);
            request.setSettingValue(this.settingValue);
        } else {
            request.setGroupSetting(this.groupSetting);
            request.setSettingValue(0);
        }

        if (this.groupControl == null) {
            request.setGroupControl(GroupControl.NO_CHANGE);
        } else {
            request.setGroupControl(this.groupControl);
        }

        if (this.groupPower == null) {
            request.setGroupPower(GroupPower.NO_CHANGE);
        } else {
            request.setGroupPower(this.groupPower);
        }

        return request;
    }

    public Request build(int messageId) {
        return GroupControlHandler.generateRequest(messageId, build());
    }
}
