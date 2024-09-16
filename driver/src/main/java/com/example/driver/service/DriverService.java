package com.example.driver.service;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.exceptions.ObjectNotValidException;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final RatingService ratingService;

    private final ObjectsValidatorImp<DriverCreateDto> objectValidator;

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

    public Double getRating(Long id){
        Driver driver=driverRepository.findById(id).get();
        Double average = (double) 0;
        for(Rating num: driver.getRatingList()){
            average+=num.getGrade();
        }
        average/=driver.getRatingList().size();
        return average;
    }

    public void deleteDriver(Long id) throws ObjectNotValidException {
        Driver driver=driverRepository.findById(id).isPresent()?driverRepository.findById(id).get():null;
        if(driver==null) throw new ObjectNotValidException(Collections.singleton("Driver not found"));
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
        return driverRepository.findAll(pageable).map(driver ->
                driverMapper.toDriverResponse(driver,getRating(driver.getId()))
        );
    }
    public List<DriverResponse> findDriversByRating(Double rating) {

       List<Rating> ratingList= ratingService.getDriverWithRating(rating);
       List<Driver> driverList = new ArrayList<>();
       ratingList.forEach(obj->driverList.add(obj.getDriver()));
       List<DriverResponse> driverResponseList = new ArrayList<>();
       driverList.forEach(obj->driverResponseList.add(driverMapper.toDriverResponse(obj,getRating(obj.getId()))));
       return driverResponseList;
    }
}
