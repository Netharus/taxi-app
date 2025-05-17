package com.example.rides.dto;

import lombok.Builder;

@Builder
public record RidesInformationResponseDto(
        Double price,
        Double distance,
        Double duration
) {
}
