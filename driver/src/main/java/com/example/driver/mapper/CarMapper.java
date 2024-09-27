package com.example.driver.mapper;

import com.example.driver.dto.CarCreateDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    Car fromCarCreateDto(CarCreateDto carCreateDto);

    CarResponseDto toCarResponseDto(Car car);

    List<CarResponseDto> toCarResponseDtos(List<Car> cars);

    Car fromCarStandaloneDto(CarStandaloneCreateDto carStandaloneCreateDto);
}
