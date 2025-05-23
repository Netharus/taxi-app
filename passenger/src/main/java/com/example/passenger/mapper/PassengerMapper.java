package com.example.passenger.mapper;

import com.example.passenger.dto.ContainerPassengerResponseDto;
import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    Passenger fromPassengerCreateDto(PassengerCreateDto passengerCreateDto);

    PassengerResponseDto toPassengerResponseDto(Passenger passenger);

    Passenger fromPassengerUpdateDto(PassengerUpdateDto passengerUpdateDto);

    @Mapping(target = "pageNum", source = "number")
    ContainerPassengerResponseDto toContainerResponseDto(Page<PassengerResponseDto> content);
}
