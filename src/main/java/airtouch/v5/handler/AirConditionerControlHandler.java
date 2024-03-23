package airtouch.v5.handler;

import java.nio.ByteBuffer;

import airtouch.Request;
import airtouch.v5.AirTouchRequest;
import airtouch.v5.builder.AirConditionerControlRequestBuilder;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.AirConditionerControlRequest;

/**
 * Handler for creating AirConditioner control messages<p>
 *
 * Example code to generate the control message to control the AC unit.<p>
 * Use {@link AirConditionerControlRequestBuilder#build()} to create a {@link AirConditionerControlRequest}
 * with useful default values.
 *
 * <pre><code>
 * AirConditionerControlRequest acControlRequest = AirConditionerControlHandler
 *      .requestBuilder()
 *          .acNumber(AC_NUMBER)
 *          .build();
 * Request request = AirConditionerControlHandler.generateRequest(MESSAGE_ID, acControlRequest);
 * </code></pre>
 */
public class AirConditionerControlHandler extends AbstractControlHandler {

    private AirConditionerControlHandler() {}

    public static Request<MessageConstants.Address> generateRequest(int messageId, AirConditionerControlRequest... acControlRequest) {

        ByteBuffer byteBuffer = assembleRequest(MessageConstants.ControlOrStatusMessageSubType.AC_CONTROL.getBytes(), acControlRequest);
        return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.CONTROL_OR_STATUS, MessageConstants.ControlOrStatusMessageSubType.AC_CONTROL, byteBuffer.array());
    }

    public static AirConditionerControlRequestBuilder requestBuilder() {
        return new AirConditionerControlRequestBuilder();
    }

    public static AirConditionerControlRequestBuilder acNumber(int acNumber) {
        return new AirConditionerControlRequestBuilder().acNumber(acNumber);
    }

}
