package com.example.driver.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RatingCreateDto(
        @NotNull
        Long driverId,
        @NotNull @Min(1) @Max(5)
        Integer grade,
        @NotNull
        Long passengerId
) {
}
