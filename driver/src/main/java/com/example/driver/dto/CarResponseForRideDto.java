package com.example.driver.dto;

import lombok.Builder;

@Builder
public record CarResponseForRideDto(
        String color,
        String brand,
        String registrationNumber
) {
}
