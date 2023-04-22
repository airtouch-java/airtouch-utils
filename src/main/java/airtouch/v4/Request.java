package airtouch.v4;

import java.nio.ByteBuffer;

import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.MessageConstants.Address;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.utils.ByteUtil;
import airtouch.v4.utils.CRC16Modbus;
import airtouch.v4.utils.HexString;

public class Request {

    private ByteBuffer buffer = ByteBuffer.allocateDirect(18); // TODO: make this more relevant.
    private Address address;
    private int messageId;
    private MessageType messageType;

    public Request(Address address, int messageId, MessageType messageType, byte[] data) {
        this.address = address;
        this.messageId = messageId;
        this.messageType = messageType;
        this.buffer.put(ByteUtil.getBytes(MessageConstants.HEADER, 2));
        this.buffer.put(address.getBytes());
        this.buffer.put(ByteUtil.getBytes(messageId, 1));
        this.buffer.put(ByteUtil.getBytes(messageType.getBytes(), 1));
        this.buffer.put(ByteUtil.getBytes(data.length, 2));
        this.buffer.put(data);
        this.buffer.put(calculateCheckSum());
    }

    private byte[] calculateCheckSum() {
        CRC16Modbus crc = new CRC16Modbus();
        crc.update(this.getRequestMessage(), 2, this.buffer.position() -2);
        return crc.getCrcBytes();
    }

    public byte[] getRequestMessage() {
        final byte[] bs = new byte[buffer.position()];
        final ByteBuffer duplicate = buffer.duplicate();
        duplicate.position(0).limit(buffer.position());
        duplicate.get(bs);
        return bs;
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
