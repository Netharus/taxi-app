package com.example.passenger.dto;

import java.util.List;

public record DriverResponseForRideDto(
        Double grade,
        String fullName,
        String phoneNumber,
        List<CarResponseForRideDto> carResponseDto
) {
}
