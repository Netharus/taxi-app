package com.example.driver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

@Builder
public record DriverCreateDto(
        @NotBlank
        String username,
        @Email
        String email,
        @NotBlank
        String fullName,
        @Pattern(
                regexp = "^\\+375(29|33|25)\\d{7}$",
                message = "Phone number must be in the format +375XX0000000, where XX is 29, 33, or 25"
        )
        String phoneNumber,
        @NotBlank
        String gender,
        List<CarCreateDto> carCreateDtoList
) {
}
