package airtouch.v5.handler;

import java.nio.ByteBuffer;

import airtouch.v5.Request;
import airtouch.v5.builder.AirConditionerControlRequestBuilder;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.AirConditionerControlRequest;

/**
 * Handler for creating AirConditioner control messages<p>
 *
 * Example code to generate the control message to control the AC unit.<p>
 * Use {@link AirConditionerControlHandler.AirConditionerControlRequestBuilder#build()} to create a {@link AirConditionerControlRequest}
 * with useful default values.
 *
 * <code><pre>
 * AirConditionerControlRequest acControlRequest = AirConditionerControlHandler
 *      .requestBuilder()
 *          .acNumber(AC_NUMBER)
 *          .build();
 * Request request = AirConditionerControlHandler.generateRequest(MESSAGE_ID, acControlRequest);
 * </pre></code>
 */
public class AirConditionerControlHandler {

    private AirConditionerControlHandler() {}

    public static Request generateRequest(int messageId, AirConditionerControlRequest... acControlRequest) {
        // Our total size of the data array will be 4 bytes per request, plus 8 bytes for the description prefix.
        int totalSize = (acControlRequest.length * 4) + 8; 
        ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
        
        byteBuffer.put((byte) MessageConstants.MessageType.AC_CONTROL.getBytes());
        byteBuffer.put((byte) 0x0); // Byte after a subMessageType is always '0x00'
        
        byteBuffer.put((byte) 0x0); // No "normal data", so these next 2 bytes are zeros. 
        byteBuffer.put((byte) 0x0);
        
        byteBuffer.put((byte) 0x0); // Our actual data is 4 bytes so add 0x0004
        byteBuffer.put((byte) 0x4);
        
        byteBuffer.put((byte) 0x0); 
        byteBuffer.put((byte) acControlRequest.length); // Number of repeats of the data.
        
        for (int i = 0; i < acControlRequest.length; i++) {
            byteBuffer.put(acControlRequest[i].getBytes());
        }
        return new Request(Address.STANDARD_SEND, messageId, MessageType.CONTROL_OR_STATUS, byteBuffer.array());
    }

    public static AirConditionerControlRequestBuilder requestBuilder() {
        return new AirConditionerControlRequestBuilder();
    }

    public static AirConditionerControlRequestBuilder acNumber(int acNumber) {
        return new AirConditionerControlRequestBuilder().acNumber(acNumber);
    }

}
