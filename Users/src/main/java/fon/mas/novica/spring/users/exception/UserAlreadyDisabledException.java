package fon.mas.novica.spring.users.exception;

public class UserAlreadyDisabledException extends RuntimeException{

    public UserAlreadyDisabledException() {
        super("User is already disabled!");
    }

    public UserAlreadyDisabledException(String message) {
        super(message);
    }

    public UserAlreadyDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyDisabledException(Throwable cause) {
        super(cause);
    }
}
