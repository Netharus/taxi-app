package com.example.passenger.dto;

import lombok.Builder;

@Builder
public record CarResponseForRideDto(
        String color,
        String brand,
        String registrationNumber
) {
}
