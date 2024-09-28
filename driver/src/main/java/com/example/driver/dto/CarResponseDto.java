package com.example.driver.dto;

public record CarResponseDto(
        Long id,
        String color,
        String brand,
        String registrationNumber
) {
}
