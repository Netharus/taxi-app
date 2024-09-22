package com.example.passenger.dto;

public record PassengerResponseDto(
        Long id,
        String firstName,
        String email,
        String phoneNumber,
        Double grade
) {
}
