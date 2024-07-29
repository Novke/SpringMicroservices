package fon.mas.novica.spring.users.advice;

import fon.mas.novica.spring.users.exception.UserAlreadyDisabledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = {
            UserAlreadyDisabledException.class,
            UsernameNotFoundException.class
    })
    ResponseEntity<?> handleBadRequest(Exception ex){
        ApiException apiException = new ApiException(ex.getMessage(), ZonedDateTime.now());
        log.warn(ex.getMessage(), ex);

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> handleServerError(Exception ex){
        ApiException apiException = new ApiException(ex.getMessage(), ZonedDateTime.now());
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
