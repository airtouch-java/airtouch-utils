package airtouch;

import java.util.List;

public interface Response<T> {

    public T getMessageType();
    public int getMessageId();
    @SuppressWarnings("rawtypes")
    public List getData();

}
