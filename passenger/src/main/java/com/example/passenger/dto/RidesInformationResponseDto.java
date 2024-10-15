package com.example.passenger.dto;

import lombok.Builder;

@Builder
public record RidesInformationResponseDto(
        Double price,
        Double distance,
        Double duration
) {
}
