package airtouch;

public interface Request<A> {
    String getHexString();

    byte[] getRequestMessage();

    ResponseMessageType getMessageType();

    int getMessageId();

    A getAddress();
    String getTypeForLog();
}
