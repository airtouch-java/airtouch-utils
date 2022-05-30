package airtouch.v4;

import java.util.List;
import java.util.ArrayList;

public class ResponseList<T> extends ArrayList<T> {

	private static final long serialVersionUID = -8195951265313899256L;

	private int messageId;

	public int getMessageId() {
		return messageId;
	}

	public ResponseList (List<T> responses , int messageId) {
		this.addAll(responses);
		this.messageId = messageId;
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
