package com.example.passenger.unit.service;

import com.example.passenger.dto.ContainerPassengerResponseDto;
import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.exceptions.ResourceNotFound;
import com.example.passenger.mapper.PassengerMapper;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import com.example.passenger.repository.PassengerRepository;
import com.example.passenger.service.PassengerService;
import com.example.passenger.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.passenger.unit.service.util.UnitTestUtils.getPassenger;
import static com.example.passenger.unit.service.util.UnitTestUtils.getPassengerCreateDto;
import static com.example.passenger.unit.service.util.UnitTestUtils.getPassengerResponseDto;
import static com.example.passenger.unit.service.util.UnitTestUtils.getPassengerUpdateDto;
import static com.example.passenger.unit.service.util.UnitTestUtils.getRating;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {

    @Spy
    PassengerMapper passengerMapper= Mappers.getMapper(PassengerMapper.class);
    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private PassengerService passengerService;

    @Test
    public void givenPassengerCreateDto_whenCreatePassenger_thenReturnPassengerResponseDto() {
        // Arrange
        PassengerCreateDto passengerCreateDto = getPassengerCreateDto();
        Passenger passenger = getPassenger();
        Rating rating = getRating();
        PassengerResponseDto expected = getPassengerResponseDto();

        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);
        when(ratingService.saveRating(any(Rating.class))).thenReturn(rating);

        //Act
        PassengerResponseDto actual = passengerService.createPassenger(passengerCreateDto);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void givenPassengerUpdateDtoAndPassengerId_whenUpdatePassenger_thenReturnPassengerResponseDto() {
        //Arrange
        PassengerUpdateDto passengerUpdateDto = getPassengerUpdateDto();
        long passengerId = 1L;
        Passenger passenger = getPassenger();
        PassengerResponseDto expected = getPassengerResponseDto();

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(passenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        //Act
        PassengerResponseDto actual = passengerService.updatePassenger(passengerUpdateDto, passengerId);

        //Assert
        assertEquals(expected, actual);
        verify(passengerRepository, times(1)).save(any(Passenger.class));
        verify(passengerRepository, times(1)).findById(passengerId);
    }

    @Test
    public void givenPassengerUpdateDtoAndPassengerId_whenUpdatePassenger_thenReturnResourceNotFound() {
        //Arrange
        PassengerUpdateDto passengerUpdateDto = getPassengerUpdateDto();
        long passengerId = 1L;

        //Act & assert
        assertThrows(ResourceNotFound.class, () -> passengerService.updatePassenger(passengerUpdateDto, passengerId));
        verify(passengerRepository, times(1)).findById(passengerId);
    }

    @Test
    public void givenPassengerId_whenFindById_thenReturnPassengerResponseDto() {
        //Arrange
        long passengerId = 1L;
        Passenger passenger = getPassenger();
        PassengerResponseDto expected = getPassengerResponseDto();
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(passenger));

        //Act
        PassengerResponseDto actual = passengerService.findById(passengerId);

        //Assert
        assertEquals(expected, actual);
        verify(passengerRepository, times(1)).findById(passengerId);
    }

    @Test
    public void givenPassengerId_whenFindById_thenReturnResourceNotFound() {
        //Arrange
        long passengerId = 1L;

        // Act & assert
        assertThrows(ResourceNotFound.class, () -> passengerService.findById(passengerId));
        verify(passengerRepository, times(1)).findById(passengerId);
    }

    @Test
    public void givenPassengerId_whenDeletePassenger_thenDeletePassenger() {
        //Arrange
        long passengerId = 1L;
        Passenger passenger = getPassenger();
        doNothing().when(ratingService).deleteByPassengerId(passengerId);
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        doNothing().when(passengerRepository).delete(passenger);

        //Act
        passengerService.deletePassenger(passengerId);

        //Assert
        verify(passengerRepository, times(1)).delete(passenger);
        verify(passengerRepository, times(1)).findById(passengerId);

    }

    @Test
    public void givenPassengerId_whenDeletePassenger_thenReturnResourceNotFound() {
        //Arrange
        long passengerId = 1L;

        //Act & assert
        assertThrows(ResourceNotFound.class, () -> passengerService.deletePassenger(passengerId));
        verify(passengerRepository, times(1)).findById(passengerId);
    }

    @Test
    public void givenPageableAndKeyword_whenFindAllByPage_thenReturnEmptyContainerPassengerResponseDto() {
        //Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = "test";
        Page<Passenger> page = new PageImpl<>(List.of(), pageable, 0);
        when(passengerRepository.findAllByPage(keyword, pageable)).thenReturn(page);
        ContainerPassengerResponseDto expected = ContainerPassengerResponseDto
                .builder()
                .content(List.of())
                .size(10)
                .totalElements(0L)
                .totalPages(0)
                .pageNum(0)
                .build();

        //Act
        ContainerPassengerResponseDto actual = passengerService.findAllByPage(pageable, keyword);

        //Assert
        assertEquals(expected, actual);
        verify(passengerRepository, times(1)).findAllByPage(keyword, pageable);
    }

    @Test
    public void givenValidRatingCreateDto_whenAddRating_thenReturnPassengerResponseDto() {
        //Arrange
        RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                .driverId(1L)
                .grade(5)
                .passengerId(1L)
                .build();
        Passenger passenger = Passenger.builder()
                .id(1L)
                .firstName("firstName")
                .email("email@email.com")
                .phoneNumber("phoneNumber")
                .grade(3.)
                .build();
        PassengerResponseDto expected = PassengerResponseDto
                .builder()
                .id(1L)
                .firstName("firstName")
                .email("email@email.com")
                .phoneNumber("phoneNumber")
                .grade(4.)
                .build();
        when(passengerRepository.findById(ratingCreateDto.passengerId())).thenReturn(Optional.of(passenger));
        when(ratingService.addRating(ratingCreateDto, passenger)).thenReturn(4.);

        //Act
        PassengerResponseDto actual = passengerService.addRating(ratingCreateDto);

        //Assert
        assertEquals(expected, actual);

        verify(passengerRepository, times(1)).findById(ratingCreateDto.driverId());
        verify(ratingService, times(1)).addRating(ratingCreateDto, passenger);
        verify(passengerRepository, times(1)).save(passenger);

    }

    @Test
    public void givenValidRatingCreateDto_whenAddRating_thenReturnResourceNotFound() {
        //Arrange
        RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                .driverId(1L)
                .grade(5)
                .passengerId(1L)
                .build();

        //Act & assert
        assertThrows(ResourceNotFound.class, () -> passengerService.addRating(ratingCreateDto));
        verify(passengerRepository, times(1)).findById(ratingCreateDto.driverId());
    }
}
