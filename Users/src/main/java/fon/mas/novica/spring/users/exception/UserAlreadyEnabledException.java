package fon.mas.novica.spring.users.exception;

public class UserAlreadyEnabledException extends RuntimeException{

    public UserAlreadyEnabledException() {
        super("User is already enabled!");
    }

    public UserAlreadyEnabledException(String message) {
        super(message);
    }

    public UserAlreadyEnabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyEnabledException(Throwable cause) {
        super(cause);
    }
}
