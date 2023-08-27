package airtouch;

import airtouch.v4.constant.MessageConstants.MessageType;

public interface Request {
    String getHexString();

    byte[] getRequestMessage();

    MessageType getMessageType();

    int getMessageId();
}
