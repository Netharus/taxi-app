package com.example.driver.unit.service;

import com.example.driver.dto.CarCreateDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.dto.ContainerDriverResponse;
import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.dto.RatingCreateDto;
import com.example.driver.exceptions.ResourceNotFound;
import com.example.driver.mapper.CarMapper;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.model.Car;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.model.enums.Gender;
import com.example.driver.repository.DriverRepository;
import com.example.driver.service.CarService;
import com.example.driver.service.DriverService;
import com.example.driver.service.RatingService;
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

import static com.example.driver.unit.service.util.UnitTestUtils.getCarCreateDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getCarResponseDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getCarStandaloneDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getDriverCreateDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getDriverUpdateDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getDriverWithId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private RatingService ratingService;

    @Mock
    private CarService carService;

    @InjectMocks
    private DriverService driverService;


    @Spy
    private DriverMapper driverMapper = Mappers.getMapper(DriverMapper.class);

    @Spy
    private CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @Test
    public void givenDriverCreateDto_whenCreateDriver_thenReturnDriverResponse() {
        CarCreateDto carCreateDto = getCarCreateDto();
        DriverCreateDto driverCreateDto = getDriverCreateDto();
        Driver driver = driverMapper.fromDriverRequest(driverCreateDto);
        driver.setId(1L);
        Car car = carMapper.fromCarCreateDto(carCreateDto);
        Rating rating = Rating.builder()
                .driver(driver)
                .grade(5)
                .build();
        CarResponseDto carResponseDto = getCarResponseDto();
        DriverResponse expectedResponse = DriverResponse.builder()
                .id(1L)
                .grade(5.)
                .username(driverCreateDto.username())
                .email(driverCreateDto.email())
                .fullName(driverCreateDto.fullName())
                .phoneNumber(driverCreateDto.phoneNumber())
                .gender(Gender.MALE)
                .carResponseDto(List.of(carResponseDto)).build();
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        when(carService.addCar(any(CarCreateDto.class), any(Driver.class))).thenReturn(car);
        when(ratingService.saveRating(any(Rating.class))).thenReturn(rating);
        when(carService.getCarsByDriverId(driver.getId())).thenReturn(List.of(carResponseDto));

        DriverResponse actualDriver = driverService.createDriver(driverCreateDto);

        assertEquals(expectedResponse, actualDriver);
        verify(carService, times(1)).addCar(any(CarCreateDto.class), any(Driver.class));
        verify(ratingService, times(1)).saveRating(any(Rating.class));
    }

    @Test
    public void givenPageableAndKeyword_whenFindAllByPage_thenReturnsContainerDriverResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = "test";
        Driver driver = new Driver();
        driver.setId(1L);
        Page<Driver> page = new PageImpl<>(List.of(), pageable, 0);
        ContainerDriverResponse expectedContainerDriverResponse = ContainerDriverResponse.builder()
                .size(10)
                .pageNum(0)
                .totalElements(0L)
                .totalPages(0)
                .driverResponses(List.of())
                .build();
        when(driverRepository.findAll(keyword, pageable)).thenReturn(page);

        ContainerDriverResponse actualContainerDriverResponse = driverService.findAllByPage(pageable, keyword);

        // Assert
        assertEquals(expectedContainerDriverResponse, actualContainerDriverResponse);
        verify(driverRepository, times(1)).findAll(keyword, pageable);
    }

    @Test
    public void givenDriverId_whenDeleteDriver_thenDeleteDriverById() {
        Driver driver = new Driver();
        driver.setId(1L);

        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        doNothing().when(carService).deleteByDriver(driver.getId());
        doNothing().when(ratingService).deleteByDriver(driver.getId());
        driverService.deleteDriver(driver.getId());

        verify(driverRepository, times(1)).findById(driver.getId());
        verify(driverRepository, times(1)).deleteById(driver.getId());

    }


    @Test
    public void givenDriverId_whenDeleteDriverNotFound_thenReturnResourceNotFound() {
        Long id = 1L;

        assertThrows(ResourceNotFound.class, () -> driverService.deleteDriver(id));
        verify(driverRepository, times(1)).findById(id);
    }

    @Test
    public void givenDriverUpdateDtoAndDriverId_whenUpdateDriver_thenReturnDriverResponse() {
        DriverUpdateDto driverUpdateDto = getDriverUpdateDto();
        Driver driver = getDriverWithId();
        CarResponseDto carResponseDto = getCarResponseDto();
        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        when(carService.getCarsByDriverId(driver.getId())).thenReturn(List.of(carResponseDto));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        DriverResponse expectedResponse = DriverResponse.builder()
                .id(1L)
                .grade(5.)
                .username(driver.getUsername())
                .email(driver.getEmail())
                .fullName(driver.getFullName())
                .phoneNumber(driver.getPhoneNumber())
                .gender(Gender.MALE)
                .carResponseDto(List.of(carResponseDto)).build();

        DriverResponse actualResponse = driverService.updateDriver(driverUpdateDto, driver.getId());

        assertEquals(expectedResponse, actualResponse);
        verify(driverRepository, times(1)).save(any(Driver.class));
        verify(carService, times(1)).getCarsByDriverId(driver.getId());
        verify(driverRepository, times(1)).findById(driver.getId());
    }

    @Test
    public void givenDriverUpdateDtoAndDriverId_whenUpdateDriverNotFound_thenReturnResourceNotFound() {
        DriverUpdateDto driverUpdateDto = getDriverUpdateDto();
        Driver driver = getDriverWithId();

        assertThrows(ResourceNotFound.class, () -> driverService.updateDriver(driverUpdateDto, driver.getId()));
        verify(driverRepository, times(1)).findById(driver.getId());
    }

    @Test
    public void givenDriverId_whenFindDriverById_thenReturnDriverResponse() {
        Driver driver = getDriverWithId();
        CarResponseDto carResponseDto = getCarResponseDto();
        DriverResponse expectedResponse = DriverResponse.builder()
                .id(1L)
                .grade(5.)
                .username(driver.getUsername())
                .email(driver.getEmail())
                .fullName(driver.getFullName())
                .phoneNumber(driver.getPhoneNumber())
                .gender(Gender.MALE)
                .carResponseDto(List.of(carResponseDto)).build();

        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        when(carService.getCarsByDriverId(driver.getId())).thenReturn(List.of(carResponseDto));

        DriverResponse actualResponse = driverService.getDriverById(driver.getId());

        assertEquals(expectedResponse, actualResponse);

        verify(driverRepository, times(1)).findById(driver.getId());
        verify(carService, times(1)).getCarsByDriverId(driver.getId());
    }

    @Test
    public void givenDriverId_whenFindDriverByIdNotFound_thenReturnResourceNotFound() {
        Long id = 1L;

        assertThrows(ResourceNotFound.class, () -> driverService.getDriverById(id));

        verify(driverRepository, times(1)).findById(id);
    }

    @Test
    public void givenValidRatingCreateDto_whenAddRating_thenSaveDriverWithUpdatedGrade() {
        RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                .driverId(1L)
                .grade(5)
                .passengerId(1L)
                .build();
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setGrade(3.0);

        when(driverRepository.findById(ratingCreateDto.driverId())).thenReturn(Optional.of(driver));
        when(ratingService.addRating(ratingCreateDto, driver)).thenReturn(4.5); // Новый рейтинг

        driverService.addRating(ratingCreateDto);

        verify(driverRepository, times(1)).findById(ratingCreateDto.driverId());
        verify(ratingService, times(1)).addRating(ratingCreateDto, driver);
        verify(driverRepository, times(1)).save(driver);

        assertEquals(4.5, driver.getGrade());
    }

    @Test
    public void givenInvalidDriverId_whenAddRating_thenThrowResourceNotFoundException() {

        RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                .driverId(1L)
                .grade(5)
                .passengerId(1L)
                .build();

        assertThrows(ResourceNotFound.class, () -> driverService.addRating(ratingCreateDto));

        verify(driverRepository, times(1)).findById(ratingCreateDto.driverId());
    }

    @Test
    public void givenCarStandAloneCreateDto_whenAddCar_thenCarResponseDto() {
        CarStandaloneCreateDto carStandaloneCreateDto = getCarStandaloneDto();
        Driver driver = getDriverWithId();
        CarResponseDto carResponseDto = getCarResponseDto();
        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        when(carService.addCar(carStandaloneCreateDto, driver)).thenReturn(carResponseDto);

        CarResponseDto actualResponseDto = driverService.addCar(carStandaloneCreateDto);

        assertEquals(carResponseDto, actualResponseDto);
        verify(driverRepository, times(1)).findById(driver.getId());
    }

    @Test
    public void givenCarStandAloneCreateDto_whenAddCar_thenThrowResourceNotFoundException() {
        CarStandaloneCreateDto carStandaloneCreateDto = getCarStandaloneDto();

        assertThrows(ResourceNotFound.class, () -> driverService.addCar(carStandaloneCreateDto));

        verify(driverRepository, times(1)).findById(carStandaloneCreateDto.driverId());
    }

}
