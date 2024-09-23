package com.example.driver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DriverCreateDto(
        @NotNull @NotEmpty
        String username,
        @Email
        String email,
        @NotNull @NotEmpty
        String fullName,
        @NotNull @NotEmpty
        String phoneNumber,
        @NotNull @NotEmpty
        String gender,

        List<CarCreateDto> carCreateDtoList) {
}
