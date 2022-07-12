package airtouch.v4.handler;

import airtouch.v4.Request;
import airtouch.v4.builder.GroupControlRequestBuilder;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.GroupControlRequest;

public class GroupControlHandler {

    private GroupControlHandler() {}

    public static Request generateRequest(int messageId, GroupControlRequest groupControlRequest) {
        byte[] data = groupControlRequest.getBytes();
        return new Request(Address.STANDARD_SEND, messageId, MessageType.GROUP_CONTROL, data);
    }

    public static GroupControlRequestBuilder requestBuilder(int groupNumber) {
        return new GroupControlRequestBuilder(groupNumber);
    }

}
