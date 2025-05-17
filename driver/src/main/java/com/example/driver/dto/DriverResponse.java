package com.example.driver.dto;

import com.example.driver.model.enums.Gender;
import lombok.Builder;

import java.util.List;

@Builder
public record DriverResponse(
        Long id,
        Double grade,
        String username,
        String email,
        String fullName,
        String phoneNumber,
        Gender gender,
        List<CarResponseDto> carResponseDto
) {
}
