package com.example.driver.service;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.model.enums.Role;
import com.example.driver.repository.DriverRepository;

import com.example.driver.validator.ObjectsValidatorImp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final RatingService ratingService;

    private final ObjectsValidatorImp objectValidator;

    //TODO Add first rating
    public DriverResponse createDriver(DriverCreateDto driverCreateDto){

        objectValidator.validate(driverCreateDto);
        Driver driver= driverMapper.fromDriverRequest(driverCreateDto);
        driver.setRole(Role.USER);
        driver.setRatingList(new ArrayList<>());
        Driver savedDriver= driverRepository.save(driver);
        Rating rating= Rating.builder().driver(savedDriver).grade(5).build();
        Rating savedRating=ratingService.saveRating(rating);
        savedDriver.getRatingList().add(savedRating);
        return driverMapper.toDriverResponse(savedDriver,getRating(savedDriver.getId()));
    }

    public List<DriverResponse> getAllDrivers() {
        List<Driver> drivers =driverRepository.findAll();
        return driverMapper.toDriverResponseList(drivers);
    }
    //TODO добавить валидацию
    public Double getRating(Long id){
        Driver driver=driverRepository.findById(id).get();
        Double average = Double.valueOf(0);
        for(Rating num: driver.getRatingList()){
            average+=num.getGrade();
        }
        average/=driver.getRatingList().size();
        return average;
    }
    //TODO добавить валидацию
    public void deleteDriver(Long id){
       ratingService.deleteByDriver(id);
        driverRepository.deleteById(id);
    }

    public DriverResponse updateDriver(DriverUpdateDto driverUpdateDto){

        Driver updateDriver=driverMapper.fromDriverUpdate(driverUpdateDto);
        Driver savedDriver=driverRepository.findById(updateDriver.getId()).get();

        savedDriver.setEmail(updateDriver.getEmail());
        savedDriver.setFullName(updateDriver.getFullName());
        savedDriver.setUsername(updateDriver.getUsername());
        savedDriver.setPhoneNumber(updateDriver.getPhoneNumber());

        return driverMapper.toDriverResponse(driverRepository.save(savedDriver),getRating(savedDriver.getId()));
    }

    public Page<DriverResponse> findAllByPage(Pageable pageable) {
        return driverRepository.findAll(pageable).map(driver -> {
            return driverMapper.toDriverResponse(driver,getRating(driver.getId()));
        });
    }


}
