package com.example.passenger.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class RequestNotValidException extends RuntimeException {
    private final Set<String> errorMassages;


}
