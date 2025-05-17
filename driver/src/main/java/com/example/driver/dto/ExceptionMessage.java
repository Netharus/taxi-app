package com.example.driver.dto;

import lombok.Builder;

@Builder
public record ExceptionMessage(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
