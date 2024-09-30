package com.example.passenger.dto;

public record RidesCreateDto(
        Long passengerId,
        String startPoint,
        String endPoint
) {
}
