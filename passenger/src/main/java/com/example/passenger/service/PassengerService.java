package com.example.passenger.service;


import com.example.passenger.clients.RidesClient;
import com.example.passenger.dto.ContainerPassengerResponseDto;
import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.dto.RideCreateResponseDto;
import com.example.passenger.dto.RidesCreateDto;
import com.example.passenger.dto.RidesInformationResponseDto;
import com.example.passenger.exceptions.ResourceNotFound;
import com.example.passenger.kafka.KafkaProducer;
import com.example.passenger.mapper.PassengerMapper;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import com.example.passenger.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PassengerService {

    private static final Double FIRST_RATING_GRADE = 5.0;

    private final PassengerRepository passengerRepository;

    private final RatingService ratingService;

    private final PassengerMapper passengerMapper;

    private final RidesClient ridesClient;

    private final static Logger logger = LoggerFactory.getLogger(PassengerService.class);

    private final KafkaProducer kafkaProducer;

    @Transactional
    public PassengerResponseDto createPassenger(PassengerCreateDto passengerCreateDto) {
        Passenger passenger = passengerMapper.fromPassengerCreateDto(passengerCreateDto);
        Passenger savedPassenger = passengerRepository.save(passenger);

        Rating rating = Rating.builder()
                .passenger(savedPassenger)
                .grade(FIRST_RATING_GRADE.intValue())
                .build();
        Rating savedRating = ratingService.saveRating(rating);
        savedPassenger.getRatingList().add(savedRating);
        savedPassenger.setGrade(FIRST_RATING_GRADE);
        passengerRepository.save(savedPassenger);

        return passengerMapper.toPassengerResponseDto(savedPassenger);
    }

    @Transactional
    public PassengerResponseDto updatePassenger(PassengerUpdateDto passengerUpdateDto, Long id) {
        Passenger updatedPassenger = passengerMapper.fromPassengerUpdateDto(passengerUpdateDto);
        Passenger existingPassenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Passenger not found"));

        existingPassenger.setEmail(updatedPassenger.getEmail());
        existingPassenger.setFirstName(updatedPassenger.getFirstName());
        existingPassenger.setPhoneNumber(updatedPassenger.getPhoneNumber());

        return passengerMapper.toPassengerResponseDto(passengerRepository.save(existingPassenger));

    }

    @Transactional(readOnly = true)
    public PassengerResponseDto findById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Passenger not found"));
        return passengerMapper.toPassengerResponseDto(passenger);
    }

    @Transactional
    public void deletePassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Passenger not found"));
        ratingService.deleteByPassengerId(id);
        passengerRepository.delete(passenger);
    }

    @Transactional(readOnly = true)
    public ContainerPassengerResponseDto findAllByPage(Pageable pageable, String keyword) {
        Page<PassengerResponseDto> page = passengerRepository
                .findAllByPage(keyword, pageable)
                .map(passengerMapper::toPassengerResponseDto);

        return passengerMapper.toContainerResponseDto(page);
    }

    @Transactional
    public PassengerResponseDto addRating(RatingCreateDto ratingCreateDto) {
        Passenger passenger = passengerRepository.findById(ratingCreateDto.passengerId())
                .orElseThrow(() -> new ResourceNotFound("Passenger not found"));
        passenger.setGrade(ratingService.addRating(ratingCreateDto, passenger));
        passengerRepository.save(passenger);
        return passengerMapper.toPassengerResponseDto(passenger);
    }

    public RidesInformationResponseDto getRideInformation(String startPoint, String endPoint) {
        return ridesClient
                .checkRidesInformation(startPoint, endPoint);
    }

    public RideCreateResponseDto createRide(RidesCreateDto ridesCreateDto) {
        return ridesClient
                .createRide(ridesCreateDto);
    }

    public void notifyPassenger(RideCreateResponseDto rideCreateResponseDto) {
        logger.info(rideCreateResponseDto.toString());
    }

    public void notifyAboutEnding(RideCreateResponseDto rideCreateResponseDto) {
        logger.info("Set grade for driver {}", rideCreateResponseDto.driverInformation().toString());
    }

    public void addRatingToPassenger(RatingCreateDto ratingCreateDto) {
        kafkaProducer.sendMessage(ratingCreateDto);
    }
}
