package airtouch;

public interface ResponseCallback<T> {

    public void handleResponse(Response<T> response);

}
