package airtouch.v4.constant;

import airtouch.v4.utils.ByteUtil;

public class MessageConstants {

	public static final int HEADER = 0x5555;

	public enum Address {
		STANDARD_SEND(0x80b0),
		EXTENDED_SEND(0x90b0),
		STANDARD_RECEIVE(0x8080),
		EXTENDED_RECEIVE(0x9090);

		private int bytes;

		Address(int bytes) {
			this.bytes = bytes;
		}

		public byte[] getBytes() {
			return ByteUtil.getBytes(this.bytes, 2);
		}
	}

	public enum MessageType {
		GROUP_CONTROL(0x2a),
		GROUP_STATUS(0x2b),
		AC_CONTROL(0x2c),
		AC_STATUS(0x2d);

		private int bytes;

		MessageType(int bytes) {
			this.bytes = bytes;
		}

		public int getBytes() {
			return bytes;
		}
	}

	public enum GroupControlMessageType {

	}


}
