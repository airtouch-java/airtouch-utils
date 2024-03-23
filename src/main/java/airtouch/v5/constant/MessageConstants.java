package airtouch.v5.constant;

import airtouch.LoggableSubType;
import airtouch.ResponseMessageType;
import airtouch.utils.ByteUtil;
import airtouch.utils.HexString;

public class MessageConstants {

    public static final int HEADER = 0x555555AA;
    public static final int KNOWN_BAD_HEADER = 0x555555AB;

    public enum Address implements ResponseMessageType {
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
                throw new IllegalArgumentException(
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
        CONTROL_OR_STATUS(0xC0),
        EXTENDED(0x1F),


//        AC_ABILITY(0xFF11),       // Extended Message sub-type
//        ZONE_NAME(0xFF13),        // Extended Message sub-type
//        CONSOLE_VERSION(0xFF30),  // Extended Message sub-type
//
        ZONE_STATUS(0x21),        // Status Message sub-type
        AC_STATUS(0x23);          // Status Message sub-type

        private int bytes;

        MessageType(int bytes) {
            this.bytes = bytes;
        }

        public byte[] getBytes() {
            return ByteUtil.getBytes(this.bytes, 1);
        }

        @Override
        public airtouch.MessageType getMessageType() {
            switch (this) {
            case CONTROL_OR_STATUS:
                return airtouch.MessageType.CONTROL_OR_STATUS;
            case EXTENDED:
                return airtouch.MessageType.EXTENDED;
            case AC_STATUS:
                return airtouch.MessageType.AC_STATUS;
            case ZONE_STATUS:
                return airtouch.MessageType.ZONE_STATUS;
            default:
                return airtouch.MessageType.UNKNOWN;
            }
        }

        public static MessageType getFromByte(int byte8) {
            if (CONTROL_OR_STATUS.bytes == byte8) {
                return CONTROL_OR_STATUS;
            } else if (EXTENDED.bytes == byte8) {
                return EXTENDED;
            } else {
                throw new IllegalArgumentException(
                        String.format("Unable to resolve MessageType from supplied byte. Supplied byte is: '%s'",
                                Integer.toHexString(byte8)));
            }
        }
    }

    public enum ControlOrStatusMessageSubType implements LoggableSubType {
        ZONE_CONTROL(0x20),
        ZONE_STATUS(0x21),
        AC_CONTROL(0x22),
        AC_STATUS(0x23);

        private int bytes;

        ControlOrStatusMessageSubType(int bytes) {
            this.bytes = bytes;
        }

        public int getBytes() {
            return bytes & 0xFF;
        }

        public static ControlOrStatusMessageSubType getFromBytes(int byte8) {
            if (ZONE_CONTROL.getBytes() == byte8) {
                return ZONE_CONTROL;
            } else if (ZONE_STATUS.getBytes() == byte8) {
                return ZONE_STATUS;
            } else if (AC_CONTROL.getBytes() == byte8) {
                return AC_CONTROL;
            } else if (AC_STATUS.getBytes() == byte8) {
                return AC_STATUS;
            } else {
                throw new IllegalArgumentException(
                        String.format("Unable to resolve ControlOrStatusMessageSubType from supplied byte. Supplied byte is: '%s'",
                                Integer.toHexString(byte8)));
            }
        }
    }

    public enum ExtendedMessageType implements ResponseMessageType, LoggableSubType {
        AC_ERROR(0xFF10),
        AC_ABILITY(0xFF11),
        ZONE_NAME(0xFF13),
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
            throw new IllegalArgumentException(String.format(
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
            case ZONE_NAME:
                return airtouch.MessageType.ZONE_NAME;
            case CONSOLE_VERSION:
                return airtouch.MessageType.CONSOLE_VERSION;
            default:
                return airtouch.MessageType.UNKNOWN;
            }
        }

    }
}
