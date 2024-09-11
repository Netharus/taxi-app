package com.example.driver.dto;

public record DriverUpdateDto(
        Long Id,
        String username,
        String email,
        String fullName,
        String phoneNumber
) {
}
