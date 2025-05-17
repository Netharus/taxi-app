package com.example.rides.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record DriverResponseForRideDto(
        Long id,
        Double grade,
        String fullName,
        String phoneNumber,
        List<CarResponseForRideDto> carResponseDto
) {
}
