package com.example.driver.service;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.repository.DriverRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final RatingService ratingService;

    //TODO Add first rating
    public DriverResponse createDriver(DriverCreateDto driverCreateDto){
        Driver driver= driverMapper.fromDriverRequest(driverCreateDto);
        Driver savedDriver= driverRepository.save(driver);
        Rating rating= Rating.builder().driver(savedDriver).grade(5).build();
        ratingService.saveRating(rating);
        return driverMapper.toDriverResponse(savedDriver);
    }

    public List<DriverResponse> getAllDrivers() {
        List<Driver> drivers =driverRepository.findAll();
        return driverMapper.toDriverResponseList(drivers);
    }
    //TODO добавить валидацию
    public Double getRating(Long id){
        Driver driver=driverRepository.findById(id).get();
        Double average = null;
        for(Rating num: driver.getRatingList()){
            average+=num.getGrade();
        }
        average/=driver.getRatingList().size();
        return average;
    }
    //TODO добавить валидацию
    public void deleteDriver(Long id){
        driverRepository.deleteById(id);
    }

    public DriverResponse updateDriver(DriverUpdateDto driverUpdateDto){
        Driver savedDriver=driverRepository.findById(driverMapper.fromDriverUpdate(driverUpdateDto).getId()).get();
        Driver updateDriver=driverMapper.fromDriverUpdate(driverUpdateDto);

        savedDriver.setEmail(updateDriver.getEmail());
        savedDriver.setFullName(updateDriver.getFullName());
        savedDriver.setUsername(updateDriver.getUsername());
        savedDriver.setPhoneNumber(updateDriver.getPhoneNumber());

        return driverMapper.toDriverResponse(driverRepository.save(savedDriver));
    }

    public Page<DriverResponse> findAllByPage(Pageable pageable) {
        return driverRepository.findAll(pageable).map(driverMapper::toDriverResponse);
    }
}
