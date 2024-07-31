package fon.mas.novica.spring.gateway.exception;

public class TokenDecoderUnavailableException extends RuntimeException{

    public TokenDecoderUnavailableException() {
    }

    public TokenDecoderUnavailableException(String message) {
        super(message);
    }

    public TokenDecoderUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenDecoderUnavailableException(Throwable cause) {
        super(cause);
    }
}
