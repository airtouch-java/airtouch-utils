package airtouch.v5.builder;

import airtouch.Request;
import airtouch.constant.ZoneControlConstants.ZoneControl;
import airtouch.constant.ZoneControlConstants.ZonePower;
import airtouch.constant.ZoneControlConstants.ZoneSetting;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.handler.ZoneControlHandler;
import airtouch.v5.model.ZoneControlRequest;

public class ZoneControlRequestBuilder {

    private Integer zoneNumber;
    private ZoneSetting zoneSetting;
    private ZoneControl zoneControl;
    private ZonePower zonePower;

    private Integer settingValue;

    /**
     * {@link ZoneControlRequestBuilder} to create a {@link ZoneControlRequest}.
     *
     * @param zoneNumber - Zone number for this control message. Zero based.
     */
    public ZoneControlRequestBuilder(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    /**
     * Method to set the {@link ZoneSetting}.<br>
     * Calling this method is optional. If not called, the request will default to ZoneSetting.NO_CHANGE.
     * If called, zoneSetting must be one of:<ul>
     * <li>NO_CHANGE - No change made to zone setting
     * <li>VALUE_DECREASE - Change value by -1°C (for set-point) or -5% (for open percentage)
     * <li>VALUE_INCREASE - Change value by +1°C (for set-point) or +5% (for open percentage)
     * <li>SET_OPEN_PERCENTAGE - Set open percentage. Also call {@link #settingValue(int)} to specify new value.
     * <li>SET_TARGET_SETPOINT - Set target set-point. Also call {@link #settingValue(int)} to specify new value.
     * </ul>
     *
     * @param zoneSetting - {@link ZoneSetting} enum value
     * @return {@link ZoneControlRequestBuilder} to support fluent builder pattern.
     */
    public ZoneControlRequestBuilder setting(ZoneSetting zoneSetting) {
        this.zoneSetting = zoneSetting;
        return this;
    }

    /**
     * Method to set the {@link ZoneControl}.<br>
     * Calling this method is optional. If not called, the request will default to ZoneControl.NO_CHANGE.
     * If called, zoneControl must be one of:<ul>
     * <li>NO_CHANGE - No change made to zone control
     * <li>TOGGLE_CONTROL_METHOD - Change from % open to °C or vice-versa
     * <li>PERCENTAGE_CONTROL - Set to use percentage open control for this zone
     * <li>TEMPERATURE_CONTROL - Set to use temperature control for this zone
     * </ul>
     * @param zoneControl - set the control method to change to.
     * @return {@link ZoneControlRequestBuilder} to support fluent builder pattern.
     */
    public ZoneControlRequestBuilder control(ZoneControl zoneControl) {
        this.zoneControl = zoneControl;
        return this;
    }

    /**
     * Method to set the {@link ZonePower}.<br>
     * Calling this method is optional. If not called, the request will default to ZonePower.NO_CHANGE.
     * If called, zonePower must be one of:<ul>
     * <li>NO_CHANGE - No change made to zone power
     * <li>NEXT_POWER_STATE - Change to next power state
     * <li>POWER_OFF - Turn off zone
     * <li>POWER_ON - Turn on zone
     * <li>TURBO_POWER - Turn zone to Turbo
     * </ul>
     * @param zonePower - set the ZonePower state to change to
     * @return {@link ZoneControlRequestBuilder} to support fluent builder pattern.
     */
    public ZoneControlRequestBuilder power(ZonePower zonePower) {
        this.zonePower = zonePower;
        return this;
    }

    /**
     * Method to set the numeric value when called in conjunction with {@link #setting(ZoneSetting)}.
     * When calling  {@link #setting(ZoneSetting)}, this method MUST be called to specify the value
     * associated with the {@link ZoneSetting}.
     *
     * @param settingValue - numeric value that matches the {@link ZoneSetting} specified by {@link #setting(ZoneSetting)}.
     * @return {@link ZoneControlRequestBuilder} to support fluent builder pattern.
     */
    public ZoneControlRequestBuilder settingValue(int settingValue) {
        this.settingValue = settingValue;
        return this;
    }

    /**
     * Build a Request with the specified messageId
     * @return Request ready to send to AirTouch
     */
    public ZoneControlRequest build() {
        ZoneControlRequest request = new ZoneControlRequest();
        request.setZoneNumber(this.zoneNumber);

        if (this.zoneSetting == null) {
            request.setZoneSetting(airtouch.v5.constant.ZoneControlConstants.ZoneSetting.NO_CHANGE);
        } else if (ZoneSetting.SET_OPEN_PERCENTAGE.equals(this.zoneSetting)
                || ZoneSetting.SET_TARGET_SETPOINT.equals(this.zoneSetting)){
            if (this.settingValue == null) {
                throw new IllegalArgumentException(
                        String.format("setting value must be defined when ZoneSettings is %s", this.zoneSetting));
            }
            request.setZoneSetting(toZoneSettingSpecific(this.zoneSetting));
            if (ZoneSetting.SET_OPEN_PERCENTAGE.equals(this.zoneSetting)) {
                request.setSettingValue(this.settingValue);
            } else if (ZoneSetting.SET_TARGET_SETPOINT.equals(this.zoneSetting)) {
                request.setSettingValue((this.settingValue  * 10) - 100);    // Convert as per page 8 of Airtouch V5 docs.
            }
        } else {
            request.setZoneSetting(toZoneSettingSpecific(this.zoneSetting));
            request.setSettingValue(0);
        }
        if (this.zoneControl == null) {
            request.setZoneControl(toZoneControlSpecific(ZoneControl.NO_CHANGE));
        } else {
            request.setZoneControl(toZoneControlSpecific(this.zoneControl));
        }

        if (this.zonePower == null) {
            request.setZonePower(toZonePowerSpecific(ZonePower.NO_CHANGE));
        } else {
            request.setZonePower(toZonePowerSpecific(this.zonePower));
        }

        return request;
    }

    private airtouch.v5.constant.ZoneControlConstants.ZonePower toZonePowerSpecific(ZonePower zonePower) {
        return airtouch.v5.constant.ZoneControlConstants.ZonePower.getSpecific(zonePower);
    }

    private airtouch.v5.constant.ZoneControlConstants.ZoneControl toZoneControlSpecific(ZoneControl zoneControl) {
        return airtouch.v5.constant.ZoneControlConstants.ZoneControl.getSpecific(zoneControl);
    }

    private airtouch.v5.constant.ZoneControlConstants.ZoneSetting toZoneSettingSpecific(ZoneSetting zoneSetting) {
        return airtouch.v5.constant.ZoneControlConstants.ZoneSetting.getSpecific(zoneSetting);
    }

    public Request<MessageConstants.Address> build(int messageId) {
        return ZoneControlHandler.generateRequest(messageId, build());
    }
}
