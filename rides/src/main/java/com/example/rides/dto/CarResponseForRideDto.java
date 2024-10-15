package com.example.rides.dto;

import lombok.Builder;

@Builder
public record CarResponseForRideDto(
        String color,
        String brand,
        String registrationNumber
) {
}
