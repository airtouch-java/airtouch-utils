package airtouch;

public interface Request<T> {
    String getHexString();

    byte[] getRequestMessage();

    T getMessageType();

    int getMessageId();
}
