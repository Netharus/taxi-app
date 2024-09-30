package com.example.rides.dto;

import java.util.List;

public record DriverResponseForRideDto(
        Long id,
        Double grade,
        String fullName,
        String phoneNumber,
        List<CarResponseForRideDto> carResponseDto
) {
}
