package airtouch.v4.exception;

public class AirtouchMessagingException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public AirtouchMessagingException(String message) {
        super(message);
    }

    public AirtouchMessagingException(String message, Throwable cause) {
        super(message, cause);
    }

}
