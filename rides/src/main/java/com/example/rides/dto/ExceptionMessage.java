package com.example.rides.dto;

public record ExceptionMessage(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
