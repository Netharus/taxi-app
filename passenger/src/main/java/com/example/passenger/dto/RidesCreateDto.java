package com.example.passenger.dto;

import lombok.Builder;

@Builder
public record RidesCreateDto(
        Long passengerId,
        String startPoint,
        String endPoint
) {
}
