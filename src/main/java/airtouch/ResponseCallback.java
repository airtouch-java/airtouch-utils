package airtouch;

public interface ResponseCallback<R,T> {
    
    public void handleResponse(Response<R,T> response);

}
