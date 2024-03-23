package airtouch.v4.constant;

import airtouch.ResponseMessageType;
import airtouch.exception.UnknownAirtouchResponseException;
import airtouch.utils.ByteUtil;
import airtouch.utils.HexString;

public class MessageConstants {

    public static final int HEADER = 0x5555;
    /**
     * Full size of all the expected header fields.
     * 
     * Header (2 bytes)
     * Address (2 bytes)
     * Message id (1 byte)
     * Message type (1 byte)
     * Data length (2 bytes)
     */
    public static final int MESSAGE_HEADER_BYTES_LENGTH = 8;
    

    public enum Address implements ResponseMessageType  {
        STANDARD_SEND(0x80b0),
        EXTENDED_SEND(0x90b0),
        STANDARD_RECEIVE(0xb080),
        EXTENDED_RECEIVE(0xb090);

        private int bytes;

        Address(int bytes) {
            this.bytes = bytes;
        }

        public int getInt() {
            return this.bytes;
        }

        public byte[] getBytes() {
            return ByteUtil.getBytes(this.bytes, 2);
        }

        public static Address getFromBytes(int l) {
            if (STANDARD_SEND.getInt() == l) {
                return STANDARD_SEND;
            } else if (EXTENDED_SEND.getInt() == l) {
                return EXTENDED_SEND;
            } else if (STANDARD_RECEIVE.getInt() == l) {
                return STANDARD_RECEIVE;
            } else if (EXTENDED_RECEIVE.getInt() == l) {
                return EXTENDED_RECEIVE;
            } else if (STANDARD_RECEIVE.getLastByte() == (l & 0x00FF)) {
                return STANDARD_RECEIVE;
            } else {
                throw new UnknownAirtouchResponseException(
                        String.format("Unable to resolve Address from supplied bytes. Supplied bytes are: '%s'",
                                HexString.fromBytes(ByteUtil.getBytes(l, 2))));
            }
        }

        private int getLastByte() {
            return this.bytes & 0x00FF;
        }

        @Override
        public airtouch.MessageType getMessageType() {
            switch (this) {
            case STANDARD_SEND:
                return airtouch.MessageType.STANDARD_SEND;
            case EXTENDED_SEND:
                return airtouch.MessageType.EXTENDED_SEND;
            case STANDARD_RECEIVE:
                return airtouch.MessageType.STANDARD_RECEIVE;
            case EXTENDED_RECEIVE:
                return airtouch.MessageType.EXTENDED_RECEIVE;
            default:
                return airtouch.MessageType.UNKNOWN;
            }
        }
    }

    public enum MessageType implements ResponseMessageType {
        GROUP_CONTROL(0x2a),
        GROUP_STATUS(0x2b),
        AC_CONTROL(0x2c),
        AC_STATUS(0x2d),
        EXTENDED(0x1F),

//        AC_ABILITY(0xFF11),       // Extended Message sub-type
//        GROUP_NAME(0xFF12),       // Extended Message sub-type
//        CONSOLE_VERSION(0xFF30);  // Extended Message sub-type
;
        private int bytes;

        MessageType(int bytes) {
            this.bytes = bytes;
        }

        public byte[] getBytes() {
            return ByteUtil.getBytes(this.bytes, 1);
        }

        public static MessageType getFromByte(byte byte6) {
            if (GROUP_CONTROL.bytes == byte6) {
                return GROUP_CONTROL;
            } else if (GROUP_STATUS.bytes == byte6) {
                return GROUP_STATUS;
            } else if (AC_CONTROL.bytes  == byte6) {
                return AC_CONTROL;
            } else if (AC_STATUS.bytes  == byte6) {
                return AC_STATUS;
            } else if (EXTENDED.bytes  == byte6) {
                return EXTENDED;
            } else {
                throw new UnknownAirtouchResponseException(
                        String.format("Unable to resolve MessageType from supplied byte. Supplied byte is: '%s'",
                                Integer.toHexString(byte6)));
            }
        }

        @Override
        public airtouch.MessageType getMessageType() {
            switch (this) {
            case GROUP_CONTROL:
                return airtouch.MessageType.ZONE_CONTROL;
            case GROUP_STATUS:
                return airtouch.MessageType.ZONE_STATUS;
            case AC_CONTROL:
                return airtouch.MessageType.AC_CONTROL;
            case AC_STATUS:
                return airtouch.MessageType.AC_STATUS;
            case EXTENDED:
                return airtouch.MessageType.EXTENDED;
            default:
                return airtouch.MessageType.UNKNOWN;
            }
        }
    }

    public enum ExtendedMessageType implements ResponseMessageType {
        AC_ERROR(0xFF10),
        AC_ABILITY(0xFF11),
        GROUP_NAME(0xFF12),
        CONSOLE_VERSION(0xFF30);

        public int getInt() {
            return this.bytes;
        }

        public byte[] getBytes() {
            return ByteUtil.getBytes(this.bytes, 2);
        }

        private int bytes;

        ExtendedMessageType(int bytes) {
            this.bytes = bytes;
        }

        public static ExtendedMessageType getFromBytes(int l) {
            for (ExtendedMessageType type: values()) {
                if (type.getInt() == l) {
                    return type;
                }
            }
            throw new UnknownAirtouchResponseException(String.format(
                    "Unable to resolve ExtendedMessageType from supplied bytes. Supplied bytes are: '%s'",
                    HexString.fromBytes(ByteUtil.getBytes(l, 2))));
        }

        @Override
        public airtouch.MessageType getMessageType() {
            switch (this) {
            case AC_ERROR:
                return airtouch.MessageType.AC_ERROR;
            case AC_ABILITY:
                return airtouch.MessageType.AC_ABILITY;
            case GROUP_NAME:
                return airtouch.MessageType.ZONE_NAME;
            case CONSOLE_VERSION:
                return airtouch.MessageType.CONSOLE_VERSION;
            default:
                return airtouch.MessageType.UNKNOWN;
            }
        }

    }
}
