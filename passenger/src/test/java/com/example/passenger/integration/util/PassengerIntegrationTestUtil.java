package com.example.passenger.integration.util;

import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerUpdateDto;

public class PassengerIntegrationTestUtil {
    public static PassengerCreateDto getPassengerCreateDto() {
        return PassengerCreateDto.builder()
                .email("email@email.com")
                .firstName("firstName")
                .phoneNumber("+375293631132")
                .build();
    }

    public static PassengerUpdateDto getPassengerUpdateDto() {
        return PassengerUpdateDto.builder()
                .email("email@email.com")
                .firstName("firstName")
                .phoneNumber("+375293631132")
                .build();
    }
}
