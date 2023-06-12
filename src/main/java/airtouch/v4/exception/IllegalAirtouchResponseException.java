package airtouch.v4.exception;

public class IllegalAirtouchResponseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IllegalAirtouchResponseException(String message) {
        super(message);
    }

}
