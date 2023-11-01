package airtouch;

import java.util.List;

public interface Response {

    public MessageType getMessageType();
    public int getMessageId();
    @SuppressWarnings("rawtypes")
    public List getData();

}
