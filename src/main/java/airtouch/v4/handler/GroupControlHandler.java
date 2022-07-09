package airtouch.v4.handler;

import airtouch.v4.Request;
import airtouch.v4.constant.GroupControlConstants.GroupControl;
import airtouch.v4.constant.GroupControlConstants.GroupPower;
import airtouch.v4.constant.GroupControlConstants.GroupSetting;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.GroupControlRequest;

public class GroupControlHandler {

    private GroupControlHandler() {}

    public static Request generateRequest(int messageId, GroupControlRequest groupControlRequest) {
        byte[] data = groupControlRequest.getBytes();
        return new Request(Address.STANDARD_SEND, messageId, MessageType.GROUP_CONTROL, data);
    }

    public static RequestBuilder requestBuilder(int messageId) {
        return new RequestBuilder(messageId);
    }

    public static class RequestBuilder {

        private Integer groupNumber;
        private GroupSetting groupSetting;
        private GroupControl groupControl;
        private GroupPower groupPower;

        private Integer settingValue;

        /**
         * {@link RequestBuilder} to create a {@link GroupControlRequest}.
         *
         * @param groupNumber - Group number for this control message. Zero based.
         */
        public RequestBuilder(int groupNumber) {
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
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder setting(GroupSetting groupSetting) {
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
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder control(GroupControl groupControl) {
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
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder power(GroupPower groupPower) {
            this.groupPower = groupPower;
            return this;
        }

        public RequestBuilder settingValue(int settingValue) {
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
    }

}
