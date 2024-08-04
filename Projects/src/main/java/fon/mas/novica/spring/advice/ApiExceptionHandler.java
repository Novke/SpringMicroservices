package fon.mas.novica.spring.advice;

import fon.mas.novica.spring.exception.*;
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
    ResponseEntity<?> handleUserNotFound(UserNotFoundException ex){
        ApiException apiException = new ApiException("User with id = " + ex.getId() + " not found!", ZonedDateTime.now());
        log.warn(ex.getMessage(), ex);

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnauthorizedActionException.class)
    ResponseEntity<?> handleUnauthorizedException(UnauthorizedActionException ex){
        ApiException apiException = new ApiException("You are not authorized for this action!", ZonedDateTime.now());
        log.debug(ex.getMessage(), ex);

        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            ProjectNotFoundException.class,
            TaskNotFoundException.class})
    ResponseEntity<?> handleEntityNotFound(RuntimeException ex){
        ApiException apiException = new ApiException(ex.getMessage(), ZonedDateTime.now());
        log.debug(ex.getMessage(), ex);

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            UsersServiceUnavailableException.class
    })
    ResponseEntity<?> handleUsersServiceUnavailableException(UsersServiceUnavailableException ex){
        ApiException apiException = new ApiException(ex.getMessage(), ZonedDateTime.now());
        log.warn(ex.getMessage(), ex);

        return new ResponseEntity<>(apiException, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
