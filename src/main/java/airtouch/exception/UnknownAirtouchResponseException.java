package airtouch.exception;

public class UnknownAirtouchResponseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnknownAirtouchResponseException(String message) {
        super(message);
    }

}
