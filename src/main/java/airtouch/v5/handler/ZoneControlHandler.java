package airtouch.v5.handler;

import airtouch.v5.Request;
import airtouch.v5.builder.ZoneControlRequestBuilder;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.ZoneControlRequest;

public class ZoneControlHandler {

    private ZoneControlHandler() {}

    public static Request generateRequest(int messageId, ZoneControlRequest zoneControlRequest) {
        byte[] data = zoneControlRequest.getBytes();
        return new Request(Address.STANDARD_SEND, messageId, MessageType.ZONE_CONTROL, data);
    }

    public static ZoneControlRequestBuilder requestBuilder(int zoneNumber) {
        return new ZoneControlRequestBuilder(zoneNumber);
    }

}
