package fon.mas.novica.spring.exception;

public class UsersServiceUnavailableException extends RuntimeException{
    public UsersServiceUnavailableException() {
    }

    public UsersServiceUnavailableException(String message) {
        super(message);
    }

    public UsersServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsersServiceUnavailableException(Throwable cause) {
        super(cause);
    }
}
