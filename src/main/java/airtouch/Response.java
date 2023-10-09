package airtouch;

import java.util.List;

public interface Response {

    public String getMessageType();
    public int getMessageId();
    @SuppressWarnings("rawtypes")
    public List getData();

}
