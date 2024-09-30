package com.example.rides.mapper;

import com.example.rides.dto.RidesCreateDto;
import com.example.rides.dto.RidesInformationResponseDto;
import com.example.rides.model.Rides;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RidesMapper {

    Rides fromRidesCreateDto(RidesCreateDto ridesCreateDto);

    RidesInformationResponseDto toRidesInformationResponseDto(Double price, Double distance, Double duration);
}
