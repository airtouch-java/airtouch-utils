package airtouch;

import java.util.List;

import java.util.ArrayList;

public class ResponseList<R> extends ArrayList<R> implements Response {

    private static final long serialVersionUID = -8195951265313899256L;

    private ResponseMessageType messageType;
    private int messageId;

    public ResponseList (ResponseMessageType messageType, int messageId, List<R> responses ) {
        this.addAll(responses);
        this.messageType = messageType;
        this.messageId = messageId;
    }

    @Override
    public int getMessageId() {
        return messageId;
    }

    @Override
    public String getMessageType() {
        return this.messageType.getMessageType();
    }

    public void setMessageType(ResponseMessageType messageType) {
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
        ResponseList<R> that = (ResponseList<R>)o;
        return this.messageId == that.messageId && super.equals(o) ;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
