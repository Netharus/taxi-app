package com.example.driver.service;

import com.example.driver.dto.CarCreateDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.exceptions.ResourceNotFound;
import com.example.driver.mapper.CarMapper;
import com.example.driver.model.Car;
import com.example.driver.model.Driver;
import com.example.driver.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    private final CarMapper carMapper;

    @Transactional
    public Car addCar(CarCreateDto carCreateDto, Driver driver) {
        Car car = carMapper.fromCarCreateDto(carCreateDto);
        car.setDriver(driver);

        return carRepository.save(car);
    }

    @Transactional
    public CarResponseDto addCar(CarStandaloneCreateDto carCreateDto, Driver driver) {
        Car car = carMapper.fromCarStandaloneDto(carCreateDto);
        car.setDriver(driver);

        return carMapper.toCarResponseDto(carRepository.save(car));
    }

    @Transactional(readOnly = true)
    public List<CarResponseDto> getCarsByDriverId(Long driverId) {
        return carMapper.toCarResponseDtos(carRepository.findByDriverId(driverId));
    }

    @Transactional
    public void deleteByDriver(Long driverId) {
        carRepository.deleteAllByDriverId(driverId);
    }

    public void deleteById(Long carId) {
        carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFound("Car not found"));
        carRepository.deleteById(carId);
    }
}
