package com.example.passenger.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class RequestNotValidException extends RuntimeException {
    private final Set<String> errorMessages;
}
