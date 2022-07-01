package airtouch.v4.handler;

import java.util.Arrays;

import airtouch.v4.Response;
import airtouch.v4.constant.MessageConstants.ExtendedMessageType;
import airtouch.v4.utils.ByteUtil;

public class ExtendedMessageHandler extends AbstractHandler {

    @SuppressWarnings("rawtypes")
    public static Response handle(int messageId, byte[] data) {
        checkHeaderIsRemoved(data);
        ExtendedMessageType extendedMessageType = ExtendedMessageType.getFromBytes(ByteUtil.toInt(data[0], data[1]));
        
        switch(extendedMessageType) {
        case GROUP_NAME:
            // Strip off the first two bytes, as they will be the 0xFF 0x12 for GroupName.
            return GroupNameHandler.handle(messageId, Arrays.copyOfRange(data, 2, data.length));
        default:
            return null;
        }
        
    }

}
