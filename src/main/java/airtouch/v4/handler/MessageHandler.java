package airtouch.v4.handler;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.exception.AirtouchResponseCrcException;
import airtouch.exception.UnknownAirtouchResponseException;
import airtouch.v4.Response;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.utils.ByteUtil;
import airtouch.v4.utils.CRC16Modbus;
import airtouch.v4.utils.HexString;

public class MessageHandler extends AbstractHandler {

    private final Logger log = LoggerFactory.getLogger(MessageHandler.class);


    @SuppressWarnings("rawtypes")
    public Response handle(byte[] airTouchMessage) {

        if (log.isDebugEnabled()) {
            log.debug("Handling Airtouch response message: hexresponse={}", HexString.fromBytes(airTouchMessage));
        }
        // Check that we are handling a message with the correct header.
        // Throws IllegalArgumentException if not valid.
        checkHeaderIsPresent(airTouchMessage);

        // Check that our message has a known Address
        // Throws IllegalArgumentException if not valid.
        Address.getFromBytes(ByteUtil.toInt(airTouchMessage, 2, 2));

        // The message is in a format we expect. Get the messageId from it.
        // We can use this to match the request we sent with the response we received if we want.
        // It's also possible to get an unsolicited response from the Airtouch4 when a state changes.
        // If that happens won't have a matching messageId.
        int messageId = airTouchMessage[4];

        // Determine the type of response we have received.
        MessageType messageType =  MessageType.getFromByte(airTouchMessage[5]);

        // Get the size of the data block. Messages can have different sized dataBlocks,
        // so read this to determine how many bytes we are going to handle.
        int dataLength = ByteUtil.toInt(airTouchMessage, 6, 2);

        // Using the dataLength, extract the expected number of bytes of data.
        byte[] data = Arrays.copyOfRange(airTouchMessage, 8, 8 + dataLength);

        if (log.isDebugEnabled()) {
            log.debug("Airtouch message: [messageId={}, type={}, dataLength={}, hexData={}]", messageId, messageType, data.length, HexString.fromBytes(data));
        }

        // After the dataBlock is the CRC. Validate the CRC.
        int crc = ByteUtil.toInt(airTouchMessage, 8 + dataLength, 2);
        long checksum = calculateChecksum(airTouchMessage, 2, airTouchMessage.length -4);
        if (checksum != crc) {
            throw new AirtouchResponseCrcException(String.format("CRC does not match calculated value. calculatedValue:'%s', fromPayload:'%s'", checksum, crc));
        }

        // Now handle the response message with the correct Handler.
        switch (messageType) {

        // Group control actions, and Group status requests will return a GROUP_STATUS response.
        case GROUP_STATUS:
            return GroupStatusHandler.handle(messageId, data);

        // AC control actions, and AC status requests will return an AC_STATUS response.
        case AC_STATUS:
            return AirConditionerStatusHandler.handle(messageId, data);

        // Extended messages (since console version 1.0.5) have the actual message type as the first
        // byte of the data, so handle those with their own Handler.
        case EXTENDED:
            return ExtendedMessageHandler.handle(messageId, data);

        // If we don't know how to handle the message, throw UnexpectedAirtouchResponseException.
        default:
            throw new UnknownAirtouchResponseException(String.format("No Handler available for type '%s'", messageType.toString()));
        }

    }

    private long calculateChecksum(byte[] airTouchDataBlock, int from, int to) {
        CRC16Modbus crc16 = new CRC16Modbus();
        crc16.update(airTouchDataBlock, from, to);
        return crc16.getValue();
    }

}
