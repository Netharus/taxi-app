package com.example.rides.mapper;

import com.example.rides.dto.DriverResponseForRideDto;
import com.example.rides.dto.RideCreateResponseDto;
import com.example.rides.dto.RideResponseForDriver;
import com.example.rides.dto.RidesCreateDto;
import com.example.rides.dto.RidesInformationResponseDto;
import com.example.rides.model.Rides;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.sql.Driver;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RidesMapper {

    Rides fromRidesCreateDto(RidesCreateDto ridesCreateDto);

    RidesInformationResponseDto toRidesInformationResponseDto(Double price, Double distance, Double duration);

    @Mapping(target = "driverInformation", source = "driverResponseForRideDto")
    @Mapping(target = "rideId", source = "id")
    RideCreateResponseDto toRideCreateResponseDto(Rides ride, DriverResponseForRideDto driverResponseForRideDto,Long id);

    @Mapping(target = "rideId", source = "id")
    RideResponseForDriver toRideResponseForDriver(Rides ride);
}
