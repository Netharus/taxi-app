package com.example.passenger.service;


import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.mapper.PassengerMapper;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import com.example.passenger.model.enums.Role;
import com.example.passenger.repository.PassengerRepository;
import com.example.passenger.repository.RatingRepository;
import com.example.passenger.validator.ObjectValidatorImp;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    private final RatingRepository ratingRepository;

    private final PassengerMapper passengerMapper;

    private final ObjectValidatorImp<PassengerCreateDto> passengerCreateDtoValidator;
    private final ObjectValidatorImp<PassengerUpdateDto> passengerUpdateDtoValidator;

    public PassengerResponseDto createPassenger(PassengerCreateDto passengerCreateDto) {

        passengerCreateDtoValidator.validate(passengerCreateDto);

        Passenger passenger = passengerMapper.fromPassengerCreateDto(passengerCreateDto);
        passenger.setRole(Role.USER);
        passenger.setRatingList(new ArrayList<>());
        Passenger savedPassenger = passengerRepository.save(passenger);
        Rating rating = Rating.builder().passenger(savedPassenger).grade(5).build();
        Rating savedRating=ratingRepository.save(rating);
        savedPassenger.getRatingList().add(savedRating);
        savedPassenger.setGrade(5.0);
        passengerRepository.save(savedPassenger);

        return passengerMapper.toPassengerResponseDto(savedPassenger);
    }

    public PassengerResponseDto updatePassenger(PassengerUpdateDto passengerUpdateDto) {
        passengerUpdateDtoValidator.validate(passengerUpdateDto);

        Passenger updatedPassenger =  passengerMapper.fromPassengerUpdateDto(passengerUpdateDto);
        Passenger existingPassenger= passengerRepository.findById(updatedPassenger.getId()).isPresent() ?
                passengerRepository.findById(updatedPassenger.getId()).get() : null;
        if(existingPassenger == null) {throw new ResourceNotFoundException("Passenger not found");}

        existingPassenger.setEmail(updatedPassenger.getEmail());
        existingPassenger.setFirstName(updatedPassenger.getFirstName());
        existingPassenger.setPhoneNumber(updatedPassenger.getPhoneNumber());

        return passengerMapper.toPassengerResponseDto(passengerRepository.save(existingPassenger));

    }
}
