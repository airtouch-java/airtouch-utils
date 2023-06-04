package airtouch.v5;

import java.util.List;

import airtouch.v5.constant.MessageConstants.MessageType;

import java.util.ArrayList;

public class ResponseList<T> extends ArrayList<T> implements Response<T> {

    private static final long serialVersionUID = -8195951265313899256L;

    private MessageType messageType;
    private int messageId;

    public ResponseList (MessageType messageType, int messageId, List<T> responses ) {
        this.addAll(responses);
        this.messageType = messageType;
        this.messageId = messageId;
    }
    
    @Override
    public int getMessageId() {
        return messageId;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }
    
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    

    @Override
    public List<T> getData() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResponseList)) return false;
        ResponseList<T> that = (ResponseList<T>)o;
        return this.messageId == that.messageId && super.equals(o) ;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
