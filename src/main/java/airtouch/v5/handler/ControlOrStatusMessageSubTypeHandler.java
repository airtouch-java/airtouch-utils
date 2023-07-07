package airtouch.v5.handler;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.v5.Response;
import airtouch.v5.constant.MessageConstants.ControlOrStatusMessageSubType;
import airtouch.utils.ByteUtil;
import airtouch.utils.HexString;

public class ControlOrStatusMessageSubTypeHandler extends AbstractHandler {

    private static final Logger log = LoggerFactory.getLogger(ControlOrStatusMessageSubTypeHandler.class);

    @SuppressWarnings("rawtypes")
    public static Response handle(int messageId, byte[] data) {
        checkHeaderIsRemoved(data);
        ControlOrStatusMessageSubType controlOrStatusMessageSubType = ControlOrStatusMessageSubType.getFromBytes(ByteUtil.toInt(data[0], data[1]));

        if (log.isDebugEnabled()) {
            log.debug("Airtouch message: [messageId={}, type={}, dataLength={}, hexData={}]", messageId, controlOrStatusMessageSubType, data.length, HexString.fromBytes(data));
        }

        switch(controlOrStatusMessageSubType) { //NOSONAR - Will add more to this switch
        case AC_STATUS:
            // Strip off the first two bytes, as they will be the 0xFF 0x13 for ZoneName.
            return AirConditionerStatusHandler.handle(messageId, Arrays.copyOfRange(data, 2, data.length));
        case ZONE_STATUS:
            // Strip off the first two bytes, as they will be the 0xFF 0x30 for Console Version.
            return ZoneStatusHandler.handle(messageId, Arrays.copyOfRange(data, 2, data.length));
        default:
            throw new UnsupportedOperationException(String.format("No Extended Handler available for type '%s'", controlOrStatusMessageSubType.toString()));
        }
    }

}
