package airtouch.exception;

public class UnknownAirtouchSubTypeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnknownAirtouchSubTypeException(String message) {
        super(message);
    }

}
