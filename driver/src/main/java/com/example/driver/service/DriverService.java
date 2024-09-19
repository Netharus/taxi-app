package com.example.driver.service;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.dto.RatingCreateDto;
import com.example.driver.exceptions.RequestNotValidException;
import com.example.driver.exceptions.ResourceNotFound;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.model.enums.Role;
import com.example.driver.repository.DriverRepository;

import com.example.driver.validator.ObjectsValidatorImp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final RatingService ratingService;

    private final ObjectsValidatorImp<DriverCreateDto> driverValidator;
    private final ObjectsValidatorImp<RatingCreateDto> ratingValidator;

    public DriverResponse createDriver(DriverCreateDto driverCreateDto) {

        driverValidator.validate(driverCreateDto);
        Driver driver = driverMapper.fromDriverRequest(driverCreateDto);
        driver.setRole(Role.USER);
        driver.setRatingList(new ArrayList<>());
        Driver savedDriver = driverRepository.save(driver);
        Rating rating = Rating.builder().driver(savedDriver).grade(5).build();
        Rating savedRating = ratingService.saveRating(rating);
        savedDriver.getRatingList().add(savedRating);
        savedDriver.setGrade(Double.valueOf(savedRating.getGrade()));
        driverRepository.save(savedDriver);
        return driverMapper.toDriverResponse(savedDriver);
    }

    public List<DriverResponse> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return driverMapper.toDriverResponseList(drivers);
    }


    public void deleteDriver(Long id) throws RequestNotValidException {
        Driver driver = driverRepository.findById(id).isPresent() ? driverRepository.findById(id).get() : null;
        if (driver == null) throw new ResourceNotFound("Driver not found");
        ratingService.deleteByDriver(id);
        driverRepository.deleteById(id);
    }

    public DriverResponse updateDriver(DriverUpdateDto driverUpdateDto) {

        Driver updateDriver = driverMapper.fromDriverUpdate(driverUpdateDto);
        Driver savedDriver = driverRepository.findById(updateDriver.getId()).isPresent() ?
                driverRepository.findById(updateDriver.getId()).get() : null;

        if (savedDriver == null) throw new ResourceNotFound("Driver not found");

        savedDriver.setGender(updateDriver.getGender());
        savedDriver.setEmail(updateDriver.getEmail());
        savedDriver.setFullName(updateDriver.getFullName());
        savedDriver.setUsername(updateDriver.getUsername());
        savedDriver.setPhoneNumber(updateDriver.getPhoneNumber());

        return driverMapper.toDriverResponse(driverRepository.save(savedDriver));
    }

    public Page<DriverResponse> findAllByPage(Pageable pageable, String keyword) {
        return driverRepository.findAll(keyword, pageable).map(driverMapper::toDriverResponse);
    }

    public Driver findById(Long aLong) {
        Driver driver=driverRepository.findById(aLong).isPresent() ? driverRepository.findById(aLong).get() : null;
        if (driver == null) throw new ResourceNotFound("Driver not found");
        return driver;

    }
    public void addRating(RatingCreateDto ratingCreateDto) {
        ratingValidator.validate(ratingCreateDto);
        Driver driver=driverRepository.findById(ratingCreateDto.driverId()).isPresent() ? driverRepository.findById(ratingCreateDto.driverId()).get() : null;
        if (driver == null) throw new ResourceNotFound("Driver not found");

        driver.setGrade(ratingService.addRating(ratingCreateDto, driver));
        driverRepository.save(driver);
    }

    public DriverResponse getDriverById(Long driverId) {
        Driver driver=driverRepository.findById(driverId).isPresent() ? driverRepository.findById(driverId).get() : null;
        if (driver == null) throw new ResourceNotFound("Driver not found");

        return driverMapper.toDriverResponse(driver);
    }

}
