package com.example.driver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DriverCreateDto(
        @NotNull @NotEmpty
        String username,
        @Email
        String email,
        @NotNull @NotEmpty
        String fullName,
        @Pattern(
                regexp = "^\\+375(29|33|25)\\d{7}$",
                message = "Phone number must be in the format +375XX0000000, where XX is 29, 33, or 25"
        )
        String phoneNumber,
        @NotNull @NotEmpty
        String gender,
        List<CarCreateDto> carCreateDtoList
) {
}
