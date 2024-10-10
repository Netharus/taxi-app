package com.example.driver.dto;

import java.util.List;

public record DriverResponseForRideDto(
        Long id,
        Double grade,
        String fullName,
        String phoneNumber,
        List<CarResponseForRideDto> carResponseDto
) {
}
