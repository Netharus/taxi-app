package com.example.passenger.service;


import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.exceptions.ResourceNotFound;
import com.example.passenger.mapper.PassengerMapper;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import com.example.passenger.model.enums.Role;
import com.example.passenger.repository.PassengerRepository;
import com.example.passenger.repository.RatingRepository;
import com.example.passenger.validator.ObjectValidatorImp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    private final RatingService ratingService;

    private final PassengerMapper passengerMapper;

    private final ObjectValidatorImp<PassengerCreateDto> passengerCreateDtoValidator;
    private final ObjectValidatorImp<PassengerUpdateDto> passengerUpdateDtoValidator;
    private final ObjectValidatorImp<RatingCreateDto> ratingCreateDtoValidator;

    public PassengerResponseDto createPassenger(PassengerCreateDto passengerCreateDto) {

        passengerCreateDtoValidator.validate(passengerCreateDto);

        Passenger passenger = passengerMapper.fromPassengerCreateDto(passengerCreateDto);
        passenger.setRole(Role.USER);
        passenger.setRatingList(new ArrayList<>());
        Passenger savedPassenger = passengerRepository.save(passenger);
        Rating rating = Rating.builder().passenger(savedPassenger).grade(5).build();
        Rating savedRating=ratingService.saveRating(rating);
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
        if(existingPassenger == null) {throw new ResourceNotFound("Passenger not found");}

        existingPassenger.setEmail(updatedPassenger.getEmail());
        existingPassenger.setFirstName(updatedPassenger.getFirstName());
        existingPassenger.setPhoneNumber(updatedPassenger.getPhoneNumber());

        return passengerMapper.toPassengerResponseDto(passengerRepository.save(existingPassenger));

    }

    public PassengerResponseDto findById(Long id) {
        Passenger passenger = passengerRepository.findById(id).isPresent()?
                passengerRepository.findById(id).get() : null;
        if (passenger == null) {throw new ResourceNotFound("Passenger not found");}
        return passengerMapper.toPassengerResponseDto(passenger);
    }

    @Transactional
    public void deletePassenger(Long id) {
        Passenger passenger= passengerRepository.findById(id).isPresent()?
                passengerRepository.findById(id).get() : null;
        if (passenger == null) {throw new ResourceNotFound("Passenger not found");}
        ratingService.deleteByPassengerId(id);
        passengerRepository.delete(passenger);
    }

    public Page<PassengerResponseDto> findAllByPage(Pageable pageable, String keyword) {
        return passengerRepository.findAllByPage(keyword,pageable).map(passengerMapper::toPassengerResponseDto);
    }

    public PassengerResponseDto addRating(RatingCreateDto ratingCreateDto) {
        ratingCreateDtoValidator.validate(ratingCreateDto);
        Passenger passenger = passengerRepository.findById(ratingCreateDto.passengerId()).isPresent()?
                passengerRepository.findById(ratingCreateDto.passengerId()).get() : null;
        if(passenger == null) {throw new ResourceNotFound("Passenger not found");}
        passenger.setGrade(ratingService.addRating(ratingCreateDto, passenger));
        passengerRepository.save(passenger);
        return passengerMapper.toPassengerResponseDto(passenger);
    }
}
