package com.example.driver.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CarStandaloneCreateDto(
        @NotNull
        Long driverId,
        @NotBlank
        String brand,
        @NotBlank
        String color,
        @Pattern(regexp = "^[0-9]{4}[A-Z]{2}[1-7]$",
                message = "Invalid license plate format. Expected format: 0000AA(1-7)")
        String registrationNumber
) {
}
