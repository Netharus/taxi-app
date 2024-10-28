package com.example.driver.unit.service;

import com.example.driver.dto.CarCreateDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.exceptions.ResourceNotFound;
import com.example.driver.model.Car;
import com.example.driver.model.Driver;
import com.example.driver.repository.CarRepository;
import com.example.driver.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.driver.unit.service.util.UnitTestUtils.getCarCreateDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getCarResponseDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getCarStandaloneDto;
import static com.example.driver.unit.service.util.UnitTestUtils.getCarWithId;
import static com.example.driver.unit.service.util.UnitTestUtils.getDriverWithId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {


    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    public void givenCarCreateDtoAndDriver_whenAddCar_thenReturnCar() {
        //Arrange
        CarCreateDto carCreateDto = getCarCreateDto();
        Driver driver = getDriverWithId();

        Car car = Car.builder()
                .id(1L)
                .brand("volvo")
                .driver(driver)
                .color("red")
                .registrationNumber("0001AM7")
                .build();

        when(carRepository.save(any(Car.class))).thenReturn(car);
        Car expectedCar = getCarWithId();
        expectedCar.setDriver(driver);

        // Act
        Car actualCar = carService.addCar(carCreateDto, driver);

        // Assert
        assertThat(actualCar)
                .usingRecursiveComparison()
                .isEqualTo(expectedCar);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    public void givenCarStandaloneCreateDtoAndDriver_whenAddCar_thenReturnCarResponseDto() {
        // Arrange
        CarStandaloneCreateDto carStandaloneCreateDto = getCarStandaloneDto();
        Driver driver = getDriverWithId();
        Car car = getCarWithId();
        car.setDriver(driver);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        CarResponseDto expectedCarResponseDto = getCarResponseDto();

        // Act
        CarResponseDto actualCarResponseDto = carService.addCar(carStandaloneCreateDto, driver);

        //Assert
        assertEquals(expectedCarResponseDto, actualCarResponseDto);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    public void givenDriverId_whenGetCarsByDriverId_thenReturnListCarResponseDto() {
        // Arrange
        Driver driver = getDriverWithId();
        Car car = getCarWithId();
        car.setDriver(driver);

        when(carRepository.findByDriverId(driver.getId())).thenReturn(List.of(car));

        List<CarResponseDto> expextedCarResponseDtoList = List.of(getCarResponseDto());

        // Act
        List<CarResponseDto> actualCarResponseDtoList = carService.getCarsByDriverId(driver.getId());

        // Assert
        assertEquals(expextedCarResponseDtoList, actualCarResponseDtoList);
        verify(carRepository, times(1)).findByDriverId(driver.getId());
    }

    @Test
    public void givenDriverId_whenDeleteByDriver_thenDeleteCar() {
        // Arrange
        long driverId = 1L;

        // Act
        carRepository.deleteAllByDriverId(driverId);

        // Assert
        verify(carRepository, times(1)).deleteAllByDriverId(driverId);
    }

    @Test
    public void givenCarId_whenDeleteCarById_thenDeleteCar() {
        // Arrange
        long carId = 1L;

        // Act
        carRepository.deleteById(carId);

        // Assert
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    public void givenCarId_whenDeleteCarById_thenReturnResourceNotFound() {
        // Arrange
        long carId = 1L;

        // Act
        assertThrows(ResourceNotFound.class, () -> carService.deleteCarById(carId));

        // Assert
        verify(carRepository, times(1)).findById(carId);
    }
}
