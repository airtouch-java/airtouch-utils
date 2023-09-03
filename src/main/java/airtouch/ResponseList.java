package airtouch;

import java.util.List;

import java.util.ArrayList;

public class ResponseList<R,T> extends ArrayList<R> implements Response<T> {

    private static final long serialVersionUID = -8195951265313899256L;

    private T messageType;
    private int messageId;

    public ResponseList (T messageType, int messageId, List<R> responses ) {
        this.addAll(responses);
        this.messageType = messageType;
        this.messageId = messageId;
    }

    @Override
    public int getMessageId() {
        return messageId;
    }

    @Override
    public T getMessageType() {
        return this.messageType;
    }

    public void setMessageType(T messageType) {
        this.messageType = messageType;
    }


    @Override
    public List<R> getData() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResponseList)) return false;
        ResponseList<R,T> that = (ResponseList<R,T>)o;
        return this.messageId == that.messageId && super.equals(o) ;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
