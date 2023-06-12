package airtouch.exception;

public class AirtouchResponseCrcException extends RuntimeException {

    public AirtouchResponseCrcException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;

}
