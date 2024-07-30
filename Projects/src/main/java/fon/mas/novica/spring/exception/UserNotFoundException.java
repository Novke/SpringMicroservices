package fon.mas.novica.spring.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{

    private final String id;

    public UserNotFoundException(String message) {
        super(message);
        id = message;
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        id = message;
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
        id = "unknown";
    }
}
