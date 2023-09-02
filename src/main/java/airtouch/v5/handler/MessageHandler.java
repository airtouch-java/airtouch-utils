package airtouch.v5.handler;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.Response;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.ControlOrStatusMessageSubType;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.model.SubMessageMetaData;
import airtouch.v5.utils.MessageEscapingUtil;
import airtouch.utils.ByteUtil;
import airtouch.utils.CRC16Modbus;
import airtouch.utils.HexString;

public class MessageHandler extends AbstractHandler {

    private final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public Response handle(byte[] airTouchMessageEscaped) {

        if (log.isDebugEnabled()) {
            log.debug("Handling Airtouch response message: hexresponse={}", HexString.fromBytes(airTouchMessageEscaped));
        }
        // Check that we are handling a message with the correct header.
        // Throws IllegalArgumentException if not valid.
        checkHeaderIsPresent(airTouchMessageEscaped);

        // Remove any redundant bytes.
        // So that the header comes through correctly, things that look like headers in the data are
        // escaped with a "00" on the end. Remove these superfluous "00" bytes.
        byte[] airTouchMessage = MessageEscapingUtil.removeMessageEscaping(airTouchMessageEscaped);

        // Check that our message has a known Address
        // Throws IllegalArgumentException if not valid.
        Address.getFromBytes(ByteUtil.toInt(airTouchMessage, 4, 2));

        // The message is in a format we expect. Get the messageId from it.
        // We can use this to match the request we sent with the response we received if we want.
        // It's also possible to get an unsolicited response from the Airtouch5 when a state changes.
        // If that happens won't have a matching messageId.
        int messageId = airTouchMessage[6];

        // Determine the type of response we have received.
        MessageType messageType =  MessageType.getFromByte((airTouchMessage[7] & 0xFF));

        // Get the size of the data block. Messages can have different sized dataBlocks,
        // so read this to determine how many bytes we are going to handle.
        int dataLength = ByteUtil.toInt(airTouchMessage, 8, 2);

        // Using the dataLength, extract the expected number of bytes of data.
        byte[] data = Arrays.copyOfRange(airTouchMessage, 10, 10 + dataLength);

        if (log.isDebugEnabled()) {
            log.debug("Airtouch message: [messageId={}, type={}, dataLength={}, hexData={}]", messageId, messageType, data.length, HexString.fromBytes(data));
        }

        // After the dataBlock is the CRC. Validate the CRC.
        int crc = ByteUtil.toInt(airTouchMessage, 10 + dataLength, 2);
        long checksum = calculateChecksum(airTouchMessage, 4, airTouchMessage.length -6);
        if (checksum != crc) {
            throw new IllegalArgumentException(String.format("CRC does not match calculated value. calculatedValue:'%s', fromPayload:'%s'", checksum, crc));
        }

        // Now handle the response message with the correct Handler.
        switch (messageType) {

        case CONTROL_OR_STATUS:
            log.debug("CONTROL_OR_STATUS");
            return handleControlOrStatus(determineSubMessageMetaData(data), messageId, Arrays.copyOfRange(data, 8, dataLength));

        // Extended messages (since console version 1.0.5) have the actual message type as the first
        // byte of the data, so handle those with their own Handler.
        case EXTENDED:
            return ExtendedMessageHandler.handle(messageId, data);

        // If we don't know how to handle the message, throw UnsupportedOperationException.
        default:
            throw new UnsupportedOperationException(String.format("No Handler available for type '%s'", messageType.toString()));
        }

    }

    private Response handleControlOrStatus(SubMessageMetaData subMessageMetaData, int messageId, byte[] data) {
        verifySubTypeData(subMessageMetaData, data);

        switch (subMessageMetaData.getSubMessageType()) {
        // Group control actions, and Group status requests will return a GROUP_STATUS response.
        case ZONE_STATUS:
            return ZoneStatusHandler.handle(subMessageMetaData, messageId, data);

        // AC control actions, and AC status requests will return an AC_STATUS response.
        case AC_STATUS:
            return AirConditionerStatusHandler.handle(messageId, data);
        // If we don't know how to handle the message, throw UnsupportedOperationException.
        default:
            throw new UnsupportedOperationException(String.format("No Handler available for type '%s'", subMessageMetaData.getSubMessageType().toString()));
        }
    }

    protected static SubMessageMetaData determineSubMessageMetaData(byte[] airTouchMessage) {
        SubMessageMetaData subMessageMetaData = new SubMessageMetaData();
        subMessageMetaData.setSubType(ControlOrStatusMessageSubType.getFromBytes(airTouchMessage[0]));
        subMessageMetaData.setNormalDataLength(ByteUtil.toInt(airTouchMessage, 2, 2));
        subMessageMetaData.setEachRepeatDataLength(ByteUtil.toInt(airTouchMessage, 4, 2));
        subMessageMetaData.setRepeatDataCount(ByteUtil.toInt(airTouchMessage, 6, 2));
        return subMessageMetaData;
    }

    private long calculateChecksum(byte[] airTouchDataBlock, int from, int to) {
        CRC16Modbus crc16 = new CRC16Modbus();
        crc16.update(airTouchDataBlock, from, to);
        return crc16.getValue();
    }

}
