package com.example.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CarStandaloneCreateDto(
        @NotNull
        Long driverId,
        @NotBlank
        String brand,
        @NotBlank
        String color,
        @NotBlank
        String registrationNumber
) {
}
