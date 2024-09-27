package com.example.driver.service;


import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.dto.ContainerDriverResponse;
import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.dto.RatingCreateDto;
import com.example.driver.exceptions.ResourceNotFound;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final static Double STARTING_GRADE = 5.;

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final RatingService ratingService;

    private final CarService carService;

    @Transactional
    public DriverResponse createDriver(DriverCreateDto driverCreateDto) {

        Driver driver = driverMapper.fromDriverRequest(driverCreateDto);
        Driver savedDriver = driverRepository.save(driver);

        if (driverCreateDto.carCreateDtoList() != null) {
            driverCreateDto.carCreateDtoList().forEach(carCreateDto -> {
                savedDriver.getCarList().add(carService.addCar(carCreateDto, savedDriver));
            });
        }

        Rating rating = Rating.builder()
                .driver(savedDriver)
                .grade(STARTING_GRADE.intValue())
                .build();
        Rating savedRating = ratingService.saveRating(rating);
        savedDriver.getRatingList().add(savedRating);
        savedDriver.setGrade(STARTING_GRADE);

        driverRepository.save(savedDriver);
        return driverMapper.toDriverResponse(savedDriver, carService.getCarsByDriverId(savedDriver.getId()));
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return driverMapper.toDriverResponseList(drivers);
    }

    @Transactional
    public void deleteDriver(Long id) {
        driverRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Driver not found"));
        ratingService.deleteByDriver(id);
        carService.deleteByDriver(id);
        driverRepository.deleteById(id);
    }

    @Transactional
    public DriverResponse updateDriver(DriverUpdateDto driverUpdateDto, Long driverId) {

        Driver updateDriver = driverMapper.fromDriverUpdate(driverUpdateDto);
        Driver savedDriver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFound("Driver not found"));

        savedDriver.setGender(updateDriver.getGender());
        savedDriver.setEmail(updateDriver.getEmail());
        savedDriver.setFullName(updateDriver.getFullName());
        savedDriver.setUsername(updateDriver.getUsername());
        savedDriver.setPhoneNumber(updateDriver.getPhoneNumber());

        return driverMapper.toDriverResponse(driverRepository.save(savedDriver),
                carService.getCarsByDriverId(savedDriver.getId()));
    }

    @Transactional(readOnly = true)
    public ContainerDriverResponse findAllByPage(Pageable pageable, String keyword) {
        Page<Driver> driversPage = driverRepository.findAll(keyword, pageable);
        return driverMapper.toContainerDriverResponse(driversPage.map(driver -> {
            List<CarResponseDto> carResponseDto = carService.getCarsByDriverId(driver.getId());
            return driverMapper.toDriverResponse(driver, carResponseDto);
        }));
    }

    @Transactional(readOnly = true)
    public Driver findById(Long aLong) {
        return driverRepository.findById(aLong).orElseThrow(() -> new ResourceNotFound("Driver not found"));

    }

    @Transactional
    public void addRating(RatingCreateDto ratingCreateDto) {

        Driver driver = driverRepository.findById(ratingCreateDto.driverId())
                .orElseThrow(()->new ResourceNotFound("Driver not found"));

        driver.setGrade(ratingService.addRating(ratingCreateDto, driver));
        driverRepository.save(driver);
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverById(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new ResourceNotFound("Driver not found"));

        return driverMapper.toDriverResponse(driver, carService.getCarsByDriverId(driverId));
    }

    @Transactional
    public CarResponseDto addCar(CarStandaloneCreateDto carCreateDto) {
        Driver driver = driverRepository.findById(carCreateDto.driverId())
                .orElseThrow(() -> new ResourceNotFound("Driver not found"));
        return carService.addCar(carCreateDto, driver);
    }
}
