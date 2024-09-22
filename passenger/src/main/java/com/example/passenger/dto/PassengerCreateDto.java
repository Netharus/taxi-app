package com.example.passenger.dto;

import jakarta.validation.constraints.*;

public record PassengerCreateDto(
        @NotBlank
        String firstName,
        @Email
        String email,
        @Pattern(
                regexp = "^\\+375(29|33|25)\\d{7}$",
                message = "Phone number must be in the format +375 XX XXX XX XX, where XX is 29, 33, or 25"
        )
        String phoneNumber
) {
}
