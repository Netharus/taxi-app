package com.example.driver.handler;

import com.example.driver.exceptions.ResourceNotFound;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class DriverExceptionHandler {

    private final static String MESSAGE = "message";

    private final static String ERROR_DETAILS_MESSAGE = "Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.";

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> handleException(ResourceNotFound exception) {
        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        String message = "Duplicate key error: " + ex.getMostSpecificCause().getMessage();
        body.put(MESSAGE, message);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, "Internal server error");
        return new ResponseEntity<>(body,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleValidationException(HttpMessageNotReadableException exception) {
        String errorDetails = "";

        if (exception.getCause() instanceof InvalidFormatException ifx) {
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                errorDetails = String.format(ERROR_DETAILS_MESSAGE,
                        ifx.getValue(), ifx.getPath().get(ifx
                                        .getPath()
                                        .size() - 1)
                                .getFieldName(),
                        Arrays.toString(ifx
                                .getTargetType()
                                .getEnumConstants()));
            }
        }
        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, errorDetails);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
