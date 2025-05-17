package com.example.passenger.dto;

import lombok.Builder;

@Builder
public record PassengerResponseDto(
        Long id,
        String firstName,
        String email,
        String phoneNumber,
        Double grade
) {
}
