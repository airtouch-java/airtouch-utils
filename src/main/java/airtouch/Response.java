package airtouch;

import java.util.List;

public interface Response<R,T> {
    
    public T getMessageType();
    public int getMessageId();
    public List<R> getData();

}
