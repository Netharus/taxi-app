package com.example.driver.util;

import com.example.driver.dto.CarCreateDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.model.Car;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.model.enums.Gender;

import java.util.List;

public class UnitTestUtils {
    public static DriverCreateDto getDriverCreateDto() {
        return DriverCreateDto.builder()
                .username("username")
                .email("email@gmail.com")
                .fullName("Full Name")
                .phoneNumber("+375331119900")
                .gender("MALE")
                .carCreateDtoList(List.of(getCarCreateDto()))
                .build();
    }

    public static DriverResponse getDriverResponseIfDbNotEmpty() {
        return DriverResponse.builder()
                .id(2L)
                .username("username")
                .email("email@gmail.com")
                .fullName("Full Name")
                .phoneNumber("+375331119900")
                .gender(Gender.MALE)
                .carResponseDto(List.of(
                        CarResponseDto.builder()
                                .id(2L)
                                .brand("volvo")
                                .color("red")
                                .registrationNumber("0021AM7")
                                .build()
                ))
                .build();
    }
    public static CarCreateDto getCarCreateDto() {
        return CarCreateDto.builder()
                .brand("volvo")
                .color("red")
                .registrationNumber("0021AM7")
                .build();
    }

    public static DriverUpdateDto getDriverUpdateDto() {
        return DriverUpdateDto.builder()
                .username("username")
                .email("email@gmail.com")
                .fullName("Full Name")
                .phoneNumber("+375331119900")
                .gender("MALE")
                .build();
    }

    public static Driver getDriverWithId() {
        return Driver.builder()
                .id(1L)
                .username("username")
                .email("email@gmail.com")
                .fullName("Full Name")
                .phoneNumber("+375331119900")
                .gender(Gender.MALE)
                .grade(5.)
                .carList(List.of(getCarWithId()))
                .build();
    }

    public static Car getCarWithId() {
        return Car.builder()
                .id(1L)
                .brand("volvo")
                .color("red")
                .registrationNumber("0001AM7")
                .build();
    }

    public static Rating getRatingWithId() {
        return Rating.builder()
                .id(1L)
                .grade(5)
                .passengerId(1L)
                .driver(getDriverWithId())
                .build();
    }

    public static CarResponseDto getCarResponseDto() {
        return CarResponseDto.builder()
                .id(1L)
                .brand("volvo")
                .color("red")
                .registrationNumber("0001AM7")
                .build();
    }

    public static CarStandaloneCreateDto getCarStandaloneDto() {
        return CarStandaloneCreateDto.builder()
                .driverId(1L)
                .brand("volvo")
                .color("red")
                .registrationNumber("0001AM7")
                .build();
    }
}
