package com.example.rides.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RidesCreateDto(
        @NotNull
        Long passengerId,
        @NotBlank
        String startPoint,
        @NotBlank
        String endPoint
) {
}
