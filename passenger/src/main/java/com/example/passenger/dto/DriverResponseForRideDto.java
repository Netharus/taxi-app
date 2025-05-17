package com.example.passenger.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DriverResponseForRideDto(
        Double grade,
        String fullName,
        String phoneNumber,
        List<CarResponseForRideDto> carResponseDto
) {
}
