package airtouch.v5.handler;

import java.nio.ByteBuffer;

import airtouch.Request;
import airtouch.v5.AirTouchRequest;
import airtouch.v5.builder.ZoneControlRequestBuilder;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.ZoneControlRequest;

public class ZoneControlHandler extends AbstractControlHandler {

    private ZoneControlHandler() {}

    public static Request<MessageType, MessageConstants.Address> generateRequest(int messageId, ZoneControlRequest zoneControlRequest) {
        ByteBuffer byteBuffer = assembleRequest(MessageConstants.ControlOrStatusMessageSubType.ZONE_CONTROL.getBytes(), zoneControlRequest);
        return new AirTouchRequest(Address.STANDARD_SEND, messageId, MessageType.CONTROL_OR_STATUS, byteBuffer.array());
    }

    public static ZoneControlRequestBuilder requestBuilder(int zoneNumber) {
        return new ZoneControlRequestBuilder(zoneNumber);
    }

}
