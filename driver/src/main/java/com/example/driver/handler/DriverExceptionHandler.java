package com.example.driver.handler;

import com.example.driver.exceptions.RequestNotValidException;
import com.example.driver.exceptions.ResourceNotFound;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class DriverExceptionHandler {

    @ExceptionHandler(RequestNotValidException.class)
    public ResponseEntity<?> handleException(RequestNotValidException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> handleException(ResourceNotFound exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        String message = "Duplicate key error: " + ex.getMostSpecificCause().getMessage();
        body.put("message", message);
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

}
