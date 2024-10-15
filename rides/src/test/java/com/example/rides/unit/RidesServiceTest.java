package com.example.rides.unit;

import com.example.rides.dto.DriverResponseForRideDto;
import com.example.rides.dto.RideCreateResponseDto;
import com.example.rides.kafka.KafkaProducer;
import com.example.rides.mapper.RidesMapper;
import com.example.rides.model.Rides;
import com.example.rides.model.enums.Status;
import com.example.rides.repository.RidesRepository;
import com.example.rides.service.RidesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RidesServiceTest {

    @Spy
    private RidesMapper ridesMapper = Mappers.getMapper(RidesMapper.class);

    @Mock
    private RidesRepository ridesRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private RidesService ridesService;

    @Mock
    private RestTemplate restTemplate;


    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenAcceptRide_thenAcceptRide() {
        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();
        Rides rides = Rides.builder()
                .id(1L)
                .driverId(1L)
                .price(1000.)
                .status(Status.ACCEPTED)
                .startPoint("startPoint")
                .endPoint("endPoint")
                .passengerId(1L)
                .build();
        long driverId = 1L;

        RideCreateResponseDto rideCreateResponseDto = ridesMapper.toRideCreateResponseDto(rides, responseForRideDto, driverId);
        when(ridesRepository.findById(rides.getId())).thenReturn(Optional.of(rides));
        when(ridesRepository.save(rides)).thenReturn(rides);

        ridesService.acceptRide(responseForRideDto, driverId);

        verify(kafkaProducer, times(1)).notifyPassenger(rideCreateResponseDto);

    }


    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenAcceptRide_thenResourceAccessException() {

        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();

        assertThrows(ResourceAccessException.class, () -> {
            ridesService.acceptRide(responseForRideDto, 1L);
        });


        verify(kafkaProducer, never()).notifyPassenger(any());
    }

    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenDeclineRide_thenAcceptRide() {
        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();
        Rides rides = Rides.builder()
                .id(1L)
                .driverId(1L)
                .price(1000.)
                .status(Status.DECLINED)
                .startPoint("startPoint")
                .endPoint("endPoint")
                .passengerId(1L)
                .build();
        long driverId = 1L;

        RideCreateResponseDto rideCreateResponseDto = ridesMapper.toRideCreateResponseDto(rides, responseForRideDto, driverId);
        when(ridesRepository.findById(rides.getId())).thenReturn(Optional.of(rides));
        when(ridesRepository.save(rides)).thenReturn(rides);

        ridesService.declineRide(responseForRideDto, driverId);

        verify(kafkaProducer, times(1)).notifyPassenger(rideCreateResponseDto);

    }


    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenDeclineRide_thenResourceAccessException() {

        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();

        assertThrows(ResourceAccessException.class, () -> {
            ridesService.declineRide(responseForRideDto, 1L);
        });


        verify(kafkaProducer, never()).notifyPassenger(any());
    }

    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenChangeStatus_thenAcceptRide() {
        Status status = Status.ON_WAY_TO_ENDPOINT;
        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();
        Rides rides = Rides.builder()
                .id(1L)
                .driverId(1L)
                .price(1000.)
                .status(status)
                .startPoint("startPoint")
                .endPoint("endPoint")
                .passengerId(1L)
                .build();
        long driverId = 1L;

        RideCreateResponseDto rideCreateResponseDto = ridesMapper.toRideCreateResponseDto(rides, responseForRideDto, driverId);
        when(ridesRepository.findById(rides.getId())).thenReturn(Optional.of(rides));
        when(ridesRepository.save(rides)).thenReturn(rides);

        ridesService.changeStatus(status, responseForRideDto, driverId);

        verify(kafkaProducer, times(1)).notifyPassenger(rideCreateResponseDto);

    }

    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenChangeStatus_thenResourceAccessException() {
        Status status = Status.ON_WAY_TO_ENDPOINT;
        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();

        assertThrows(ResourceAccessException.class, () -> {
            ridesService.changeStatus(status, responseForRideDto, 1L);
        });


        verify(kafkaProducer, never()).notifyPassenger(any());
    }


    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenEndDrive_thenAcceptRide() {
        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();
        Rides rides = Rides.builder()
                .id(1L)
                .driverId(1L)
                .price(1000.)
                .status(Status.COMPLETED)
                .startPoint("startPoint")
                .endPoint("endPoint")
                .passengerId(1L)
                .build();
        long driverId = 1L;

        RideCreateResponseDto rideCreateResponseDto = ridesMapper.toRideCreateResponseDto(rides, responseForRideDto, driverId);
        when(ridesRepository.findById(rides.getId())).thenReturn(Optional.of(rides));
        when(ridesRepository.save(rides)).thenReturn(rides);

        ridesService.endDrive(responseForRideDto, driverId);

        verify(kafkaProducer, times(1)).notifyPassenger(rideCreateResponseDto);
        verify(kafkaProducer, times(1)).notifyDriver(ridesMapper.toRideResponseForDriver(rides));

    }


    @Test
    public void givenDriverResponseForRideDtoAndRideId_whenEndDrive_thenResourceAccessException() {

        DriverResponseForRideDto responseForRideDto = DriverResponseForRideDto
                .builder()
                .carResponseDto(List.of())
                .id(1L)
                .grade(5.)
                .fullName("fullName")
                .build();

        assertThrows(ResourceAccessException.class, () -> {
            ridesService.endDrive(responseForRideDto, 1L);
        });


        verify(kafkaProducer, never()).notifyPassenger(any());
    }
}
