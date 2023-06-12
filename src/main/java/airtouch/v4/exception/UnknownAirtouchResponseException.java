package airtouch.v4.exception;

public class UnknownAirtouchResponseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnknownAirtouchResponseException(String message) {
        super(message);
    }

}
