package airtouch.v4.builder;

import airtouch.Request;
import airtouch.v4.constant.AirConditionerControlConstants.AcPower;
import airtouch.v4.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.v4.constant.AirConditionerControlConstants.Mode;
import airtouch.v4.constant.AirConditionerControlConstants.SetpointControl;
import airtouch.v4.handler.AirConditionerControlHandler;
import airtouch.v4.model.AirConditionerControlRequest;

public class AirConditionerControlRequestBuilder {

        private Integer acNumber;
        private AcPower acPower;
        private Mode acMode;
        private FanSpeed fanSpeed;
        private SetpointControl setpointControl;
        private Integer setPointValue;

        /**
         * {@link AirConditionerControlRequestBuilder} to create an {@link AirConditionerControlRequest}.
         *
         * @param acNumber - AC number for this control message. Zero based.
         */
        public AirConditionerControlRequestBuilder acNumber(int acNumber) {
            this.acNumber = acNumber;
            return this;
        }

        /**
         * Method to set the {@link AcPower}.<br>
         * Calling this method is optional. If not called, the request will default to AcPower.NO_CHANGE.
         * If called, acPower must be one of:<ul>
         * <li>NO_CHANGE - No change made to power setting.
         * <li>NEXT_POWER_STATE - Change toggle from OFF to ON or vice versa.
         * <li>POWER_OFF - Turn the AC unit off.
         * <li>POWER_ON - Turn the AC unit on.
         * </ul>
         *
         * @param acPower - {@link AcPower} enum value
         * @return {@link AirConditionerControlRequestBuilder} to support fluent builder pattern.
         */
        public AirConditionerControlRequestBuilder acPower(AcPower acPower) {
            this.acPower = acPower;
            return this;
        }

        /**
         * Method to set the {@link Mode}.<br>
         * Calling this method is optional. If not called, the request will default to Mode.NO_CHANGE.
         * If called, groupControl must be one of:<ul>
         * <li>AUTO - AC in Auto Heat/Cool mode.
         * <li>HEAT - AC set to Heating mode.
         * <li>DRY - AC set to Drying mode.
         * <li>FAN - AC runs the fan only
         * <li>COOL - AC set to Cooling mode.
         * <li>NO_CHANGE - No change made to AC mode
         * </ul>
         * @param acMode
         * @return {@link AirConditionerControlRequestBuilder} to support fluent builder pattern.
         */
        public AirConditionerControlRequestBuilder acMode(Mode acMode) {
            this.acMode = acMode;
            return this;
        }

        /**
         * Method to set the {@link FanSpeed}.<br>
         * Calling this method is optional. If not called, the request will default to FanSpeed.NO_CHANGE.
         * If called, groupPower must be one of:<ul>
         * <li>AUTO - Set Fan to Automatic speed control
         * <li>QUIET - Set Fan to Quiet speed
         * <li>LOW - Set Fan to Low speed
         * <li>MEDIUM - Set Fan to Medium speed
         * <li>HIGH - Set Fan to High speed
         * <li>POWERFUL - Set Fan to Powerful speed
         * <li>TURBO - Set Fan to Turbo speed
         * </ul>
         * @param fanSpeed
         * @return {@link AirConditionerControlRequestBuilder} to support fluent builder pattern.
         */
        public AirConditionerControlRequestBuilder fanSpeed(FanSpeed fanSpeed) {
            this.fanSpeed = fanSpeed;
            return this;
        }

        /**
         * Method to set the {@link SetpointControl}.<br>
         * Calling this method is optional. If not called, the request will default to SetpointControl.NO_CHANGE.
         * If called, setpointControl must be one of:<ul>
         * <li>NO_CHANGE - No change made to group setting
         * <li>SET_TO_VALUE -
         * <li>VALUE_DECREASE - Change set-point by -1°C
         * <li>VALUE_INCREASE - Change set-point by +1°C
         * </ul>
         * @param setpointControl
         * @return {@link AirConditionerControlRequestBuilder} to support fluent builder pattern.
         */
        public AirConditionerControlRequestBuilder setpointControl(SetpointControl setpointControl) {
            this.setpointControl = setpointControl;
            return this;
        }

        /**
         * Method to set the setpoint value.
         * Has no effect unless {@link #setpointControl(SetpointControl.SET_TO_VALUE)} is
         * also called.
         * @param setPointValue
         * @return {@link AirConditionerControlRequestBuilder} to support fluent builder pattern.
         */
        public AirConditionerControlRequestBuilder setpointValue(int setPointValue) {
            this.setPointValue = setPointValue;
            return this;
        }

        public AirConditionerControlRequest build() {
            AirConditionerControlRequest request = new AirConditionerControlRequest();

            if (this.acNumber == null || this.acNumber < 0 || this.acNumber > 3) {
                throw new IllegalArgumentException(
                        String.format("acNumber value must be a number between 0 and 3. Value %s is incorrect", this.acNumber));
            } else {
                request.setAcNumber(this.acNumber);
            }

            if (this.acPower == null) {
                request.setAcPower(AcPower.NO_CHANGE);
            } else {
                request.setAcPower(this.acPower);
            }

            if (this.acMode == null) {
                request.setAcMode(Mode.NO_CHANGE);
            } else {
                request.setAcMode(this.acMode);
            }

            if (this.fanSpeed == null) {
                request.setFanSpeed(FanSpeed.NO_CHANGE);
            } else {
                request.setFanSpeed(this.fanSpeed);
            }

            if (this.setpointControl == null) {
                request.setSetpointControl(SetpointControl.NO_CHANGE);
            } else if (SetpointControl.SET_TO_VALUE.equals(this.setpointControl)) {
                if (this.setPointValue == null) {
                    throw new IllegalArgumentException(
                            String.format("setting value must be defined when SetpointControl is %s", this.setpointControl));
                }
                request.setSetpointControl(this.setpointControl);
                request.setSetpointValue(this.setPointValue);
            } else {
                request.setSetpointControl(this.setpointControl);
                request.setSetpointValue(0);
            }
            return request;
        }
        
        public Request build(int messageId) {
            return AirConditionerControlHandler.generateRequest(messageId, build());
        }
    }
