package com.example.passenger.service;


import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.mapper.PassengerMapper;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import com.example.passenger.model.enums.Role;
import com.example.passenger.repository.PassengerRepository;
import com.example.passenger.repository.RatingRepository;
import com.example.passenger.validator.ObjectValidatorImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    private final RatingRepository ratingRepository;

    private final PassengerMapper passengerMapper;

    private final ObjectValidatorImp<PassengerCreateDto> passengerCreateDtoValidator;

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
}
