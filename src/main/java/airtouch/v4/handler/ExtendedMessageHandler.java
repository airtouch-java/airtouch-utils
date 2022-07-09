package airtouch.v4.handler;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.v4.Response;
import airtouch.v4.constant.MessageConstants.ExtendedMessageType;
import airtouch.v4.utils.ByteUtil;
import airtouch.v4.utils.HexString;

public class ExtendedMessageHandler extends AbstractHandler {

    private static final Logger log = LoggerFactory.getLogger(ExtendedMessageHandler.class);

    @SuppressWarnings("rawtypes")
    public static Response handle(int messageId, byte[] data) {
        checkHeaderIsRemoved(data);
        ExtendedMessageType extendedMessageType = ExtendedMessageType.getFromBytes(ByteUtil.toInt(data[0], data[1]));

        if (log.isDebugEnabled()) {
            log.debug("Airtouch message: [messageId={}, type={}, dataLength={}, hexData={}]", messageId, extendedMessageType, data.length, HexString.fromBytes(data));
        }

        switch(extendedMessageType) { //NOSONAR - Will add more to this switch
        case GROUP_NAME:
            // Strip off the first two bytes, as they will be the 0xFF 0x12 for GroupName.
            return GroupNameHandler.handle(messageId, Arrays.copyOfRange(data, 2, data.length));
        default:
            throw new UnsupportedOperationException(String.format("No Extended Handler available for type '%s'", extendedMessageType.toString()));
        }

    }

}
