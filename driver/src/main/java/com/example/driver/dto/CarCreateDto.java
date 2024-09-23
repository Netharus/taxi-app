package com.example.driver.dto;

import jakarta.validation.constraints.NotBlank;

public record CarCreateDto(
        @NotBlank
        String brand,
        @NotBlank
        String color,
        @NotBlank
        String registrationNumber
) {
}
