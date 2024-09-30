package com.example.driver.dto;

import java.util.List;

public record DriverResponseForRideDto(
        Double grade,
        String fullName,
        String phoneNumber,
        List<CarResponseForRideDto> carResponseDto
) {
}
