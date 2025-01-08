package com.nourish1709.learning.configservice.errors.handler;

import com.nourish1709.learning.configservice.errors.PropertyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<?> handlePropertyNotFoundException(PropertyNotFoundException e) {
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }
}
