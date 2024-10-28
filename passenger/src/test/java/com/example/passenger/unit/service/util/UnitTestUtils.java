package com.example.passenger.unit.service.util;

import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;

public class UnitTestUtils {
    public static PassengerCreateDto getPassengerCreateDto() {
        return PassengerCreateDto.builder()
                .email("email@email.com")
                .firstName("firstName")
                .phoneNumber("phoneNumber")
                .build();
    }

    public static PassengerUpdateDto getPassengerUpdateDto() {
        return PassengerUpdateDto.builder()
                .email("email@email.com")
                .firstName("firstName")
                .phoneNumber("phoneNumber")
                .build();
    }

    public static PassengerResponseDto getPassengerResponseDto() {
        return PassengerResponseDto.builder()
                .id(1L)
                .firstName("firstName")
                .email("email@email.com")
                .phoneNumber("phoneNumber")
                .grade(5.)
                .build();
    }

    public static Passenger getPassenger() {
        return Passenger.builder()
                .id(1L)
                .firstName("firstName")
                .email("email@email.com")
                .phoneNumber("phoneNumber")
                .grade(5.)
                .build();
    }

    public static Rating getRating() {
        return Rating.builder()
                .passenger(getPassenger())
                .grade(5)
                .driverId(1L)
                .build();
    }
}
