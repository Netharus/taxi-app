package com.example.passenger.mapper;

import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    Passenger fromPassengerCreateDto(PassengerCreateDto passengerCreateDto);

    PassengerResponseDto toPassengerResponseDto(Passenger passenger);

    Passenger fromPassengerUpdateDto(PassengerUpdateDto passengerUpdateDto);
}
