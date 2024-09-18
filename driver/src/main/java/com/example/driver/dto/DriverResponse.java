package com.example.driver.dto;

import com.example.driver.model.Rating;

import java.util.List;

public record DriverResponse(
        Long id,
        Double grade,
        String username,
        String email,
        String fullName,
        String phoneNumber) {
}
