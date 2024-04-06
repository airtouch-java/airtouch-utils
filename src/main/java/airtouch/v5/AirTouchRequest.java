package airtouch.v5;

import java.nio.ByteBuffer;

import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.ControlOrStatusMessageSubType;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.utils.MessageEscapingUtil;
import airtouch.LoggableSubType;
import airtouch.Request;
import airtouch.utils.ByteUtil;
import airtouch.utils.CRC16Modbus;
import airtouch.utils.HexString;

public class AirTouchRequest implements Request<MessageConstants.Address> {

    private ByteBuffer buffer = ByteBuffer.allocateDirect(64); // TODO: make this more relevant.
    private Address address;
    private int messageId;
    private MessageType messageType;
    private LoggableSubType subType = null;

    public AirTouchRequest(Address address, int messageId, MessageType messageType, LoggableSubType loggableSubType, byte[] data) {
        this.address = address;
        this.messageId = messageId;
        this.messageType = messageType;
        this.buffer.put(ByteUtil.getBytes(MessageConstants.HEADER, 4));
        this.buffer.put(address.getBytes());
        this.buffer.put(ByteUtil.getBytes(messageId, 1));
        this.buffer.put(messageType.getBytes());
        this.buffer.put(ByteUtil.getBytes(data.length, 2));
        this.buffer.put(data);
        this.buffer.put(calculateCheckSum());
    }

    /**
     * Creates an AirTouch request for {@link ControlOrStatusMessageSubType}.<br>
     * These types have extra padded values of 0x00 in the message.
     * @param address - message category for this request
     * @param messageId - messageId to use with this request
     * @param messageType - type for this request
     * @param subType - sub-type for this request
     */
    public AirTouchRequest(Address address, int messageId, MessageType messageType, ControlOrStatusMessageSubType subType) {
        this.address = address;
        this.messageId = messageId;
        this.messageType = messageType;
        this.subType = subType;
        this.buffer.put(ByteUtil.getBytes(MessageConstants.HEADER, 4));
        this.buffer.put(address.getBytes());
        this.buffer.put(ByteUtil.getBytes(messageId, 1));
        this.buffer.put(messageType.getBytes());
        byte[] data = new byte[] { (byte) subType.getBytes(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        this.buffer.put(ByteUtil.getBytes(data.length, 2));
        this.buffer.put(data);
        this.buffer.put(calculateCheckSum());
    }

    private byte[] calculateCheckSum() {
        CRC16Modbus crc = new CRC16Modbus();
        crc.update(this.getUnEscapedMessage(), 4, this.buffer.position() -4);
        return crc.getCrcBytes();
    }

    private byte[] getUnEscapedMessage() {
        final byte[] bs = new byte[buffer.position()];
        final ByteBuffer duplicate = buffer.duplicate();
        duplicate.position(0).limit(buffer.position());
        duplicate.get(bs);
        return bs;
    }
    public byte[] getRequestMessage() {
        return MessageEscapingUtil.addMessageEscaping(getUnEscapedMessage());
    }

    public String getHexString() {
        return HexString.fromBytes(getRequestMessage());
    }

    public Address getAddress() {
        return address;
    }

    public int getMessageId() {
        return messageId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public static byte b(int i) {
        return (byte) (i & 0xFF);
    }

    @Override
    public String getTypeForLog() {
        return this.subType == null 
                ? this.messageType.toString() 
                : this.messageType.toString() + "." + this.subType.toString();
    }

}
