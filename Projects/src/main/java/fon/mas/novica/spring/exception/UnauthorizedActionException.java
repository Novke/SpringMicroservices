package fon.mas.novica.spring.exception;

public class UnauthorizedActionException extends RuntimeException{
    public UnauthorizedActionException() {
    }

    public UnauthorizedActionException(String message) {
        super(message);
    }

    public UnauthorizedActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedActionException(Throwable cause) {
        super(cause);
    }
}
