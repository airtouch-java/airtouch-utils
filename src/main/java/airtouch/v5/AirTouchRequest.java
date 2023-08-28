package airtouch.v5;

import java.nio.ByteBuffer;

import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;
import airtouch.v5.constant.MessageConstants.ControlOrStatusMessageSubType;
import airtouch.v5.constant.MessageConstants.MessageType;
import airtouch.v5.utils.MessageEscapingUtil;
import airtouch.Request;
import airtouch.utils.ByteUtil;
import airtouch.utils.CRC16Modbus;
import airtouch.utils.HexString;

public class AirTouchRequest implements Request<MessageType> {

    private ByteBuffer buffer = ByteBuffer.allocateDirect(64); // TODO: make this more relevant.
    private Address address;
    private int messageId;
    private MessageType messageType;

    public AirTouchRequest(Address address, int messageId, MessageType messageType, byte[] data) {
        this.address = address;
        this.messageId = messageId;
        this.messageType = messageType;
        this.buffer.put(ByteUtil.getBytes(MessageConstants.HEADER, 4));
        this.buffer.put(address.getBytes());
        this.buffer.put(ByteUtil.getBytes(messageId, 1));
        this.buffer.put(ByteUtil.getBytes(messageType.getBytes(), 1));
        this.buffer.put(ByteUtil.getBytes(data.length, 2));
        this.buffer.put(data);
        this.buffer.put(calculateCheckSum());
    }
    
    public AirTouchRequest(Address address, int messageId, MessageType messageType, ControlOrStatusMessageSubType subType) {
        this.address = address;
        this.messageId = messageId;
        this.messageType = messageType;
        this.buffer.put(ByteUtil.getBytes(MessageConstants.HEADER, 4));
        this.buffer.put(address.getBytes());
        this.buffer.put(ByteUtil.getBytes(messageId, 1));
        this.buffer.put(ByteUtil.getBytes(messageType.getBytes(), 1));
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

}
