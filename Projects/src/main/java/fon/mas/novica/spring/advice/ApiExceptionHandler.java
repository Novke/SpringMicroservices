package fon.mas.novica.spring.advice;

import fon.mas.novica.spring.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<?> handleNotFound(UserNotFoundException ex){
        ApiException apiException = new ApiException("User with id = " + ex.getId() + " not found!", ZonedDateTime.now());
        log.warn(ex.getMessage(), ex);

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

}
