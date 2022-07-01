package airtouch.v4.handler;

import airtouch.v4.Request;
import airtouch.v4.constant.AirConditionerControlConstants.*;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.AirConditionerControlRequest;

/**
 * Handler for creating AirConditioner control messages<p>
 *
 * Example code to generate the control message to control the AC unit.<p>
 * Use {@link AirConditionerControlHandler.RequestBuilder#build()} to create a {@link AirConditionerControlRequest}
 * with useful default values.
 *
 * <code><pre>
 * AirConditionerControlRequest acControlRequest = new AirConditionerControlHandler
 *      .RequestBuilder(AC_NUMBER)
 *          .build();
 * Request request = AirConditionerControlHandler.generateRequest(MESSAGE_ID, acControlRequest);
 * </pre></code>
 * a
 */
public class AirConditionerControlHandler {
    
    private AirConditionerControlHandler() {}
    
    public static Request generateRequest(int messageId, AirConditionerControlRequest acControlRequest) {
        byte[] data = acControlRequest.getBytes();
        return new Request(Address.STANDARD_SEND, messageId, MessageType.AC_CONTROL, data);
    }

    public static class RequestBuilder {
        
        private Integer acNumber;
        private Power acPower;
        private Mode acMode;
        private FanSpeed fanSpeed;
        private SetpointControl setpointControl;
        private Integer setPointValue;
        
        /**
         * {@link RequestBuilder} to create an {@link AirConditionerControlRequest}.
         *
         * @param acNumber - AC number for this control message. Zero based.
         */
        public RequestBuilder(int acNumber) {
            this.acNumber = acNumber;
        }
        
        /**
         * Method to set the {@link Power}.<br>
         * Calling this method is optional. If not called, the request will default to AcPower.NO_CHANGE.
         * If called, acPower must be one of:<ul>
         * <li>NO_CHANGE - No change made to power setting.
         * <li>NEXT_POWER_STATE - Change toggle from OFF to ON or vice versa.
         * <li>POWER_OFF - Turn the AC unit off.
         * <li>POWER_ON - Turn the AC unit on.
         * </ul>
         *
         * @param acPower - {@link Power} enum value
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder acPower(Power acPower) {
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
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder acMode(Mode acMode) {
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
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder fanSpeed(FanSpeed fanSpeed) {
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
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder setpointControl(SetpointControl setpointControl) {
            this.setpointControl = setpointControl;
            return this;
        }
        
        /**
         * Method to set the setpoint value.
         * Has no effect unless {@link #setpointControl(SetpointControl.SET_TO_VALUE)} is
         * also called.
         * @param setPointValue
         * @return {@link RequestBuilder} to support fluent builder pattern.
         */
        public RequestBuilder setpointValue(int setPointValue) {
            this.setPointValue = setPointValue;
            return this;
        }
        
        public AirConditionerControlRequest build() {
            AirConditionerControlRequest request = new AirConditionerControlRequest();
            request.setAcNumber(this.acNumber);
            
            if (this.acPower == null) {
                request.setAcPower(Power.NO_CHANGE);
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
    }

}
