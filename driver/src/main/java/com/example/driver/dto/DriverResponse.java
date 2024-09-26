package com.example.driver.dto;

import com.example.driver.model.enums.Gender;

import java.util.List;

public record DriverResponse(
        Long id,
        Double grade,
        String username,
        String email,
        String fullName,
        String phoneNumber,
        Gender gender,
        List<CarResponseDto> carResponseDto) {
}
