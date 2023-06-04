package airtouch.v5;

import java.util.List;

import airtouch.v5.constant.MessageConstants.MessageType;

public interface Response<T> {
    
    public MessageType getMessageType();
    public int getMessageId();
    public List<T> getData();

}
