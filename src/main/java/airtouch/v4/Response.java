package airtouch.v4;

import java.util.List;

import airtouch.v4.constant.MessageConstants.MessageType;

public interface Response<T> {
    
    public MessageType getMessageType();
    public int getMessageId();
    public List<T> getData();

}
