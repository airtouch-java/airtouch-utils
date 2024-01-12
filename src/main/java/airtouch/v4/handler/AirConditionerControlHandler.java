package airtouch.v4.handler;

import airtouch.Request;
import airtouch.v4.AirTouchRequest;
import airtouch.v4.builder.AirConditionerControlRequestBuilder;
import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.AirConditionerControlRequest;

/**
 * Handler for creating AirConditioner control messages<p>
 *
 * Example code to generate the control message to control the AC unit.<p>
 * Use {@link AirConditionerControlRequestBuilder#build()} to create a {@link AirConditionerControlRequest}
 * with useful default values.
 *
 *<pre><code>
 * AirConditionerControlRequest acControlRequest = AirConditionerControlHandler
 *      .requestBuilder()
 *          .acNumber(AC_NUMBER)
 *          .build();
 * Request request = AirConditionerControlHandler.generateRequest(MESSAGE_ID, acControlRequest);
 * </code></pre>
 */
public class AirConditionerControlHandler {

    private AirConditionerControlHandler() {}

    public static Request<MessageConstants.Address> generateRequest(int messageId, AirConditionerControlRequest acControlRequest) {
        byte[] data = acControlRequest.getBytes();
        return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.AC_CONTROL, data);
    }

    public static AirConditionerControlRequestBuilder requestBuilder() {
        return new AirConditionerControlRequestBuilder();
    }

    public static AirConditionerControlRequestBuilder requestBuilder(int acNumber) {
        return new AirConditionerControlRequestBuilder().acNumber(acNumber);
    }

}
