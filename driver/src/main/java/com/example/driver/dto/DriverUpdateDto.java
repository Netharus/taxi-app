package com.example.driver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DriverUpdateDto(
        @NotBlank
        String username,
        @Email
        String email,
        @NotBlank
        String fullName,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String gender
) {
}
