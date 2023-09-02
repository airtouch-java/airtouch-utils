package airtouch;

public interface Request<T,A> {
    String getHexString();

    byte[] getRequestMessage();

    T getMessageType();

    int getMessageId();

    A getAddress();
}
