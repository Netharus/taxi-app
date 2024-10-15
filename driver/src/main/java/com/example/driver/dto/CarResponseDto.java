package com.example.driver.dto;

import lombok.Builder;

@Builder
public record CarResponseDto(
        Long id,
        String color,
        String brand,
        String registrationNumber
) {
}
