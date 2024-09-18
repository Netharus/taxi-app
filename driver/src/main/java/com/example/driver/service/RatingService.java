package com.example.driver.service;

import com.example.driver.dto.RatingCreateDto;
import com.example.driver.exceptions.ResourceNotFound;
import com.example.driver.mapper.RatingMapper;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    private final RatingMapper ratingMapper;

    public Rating saveRating(Rating rating){
        return ratingRepository.save(rating);
    }
    @Transactional
    public void deleteByDriver(Long driverId){
        ratingRepository.deleteAllByDriverId(driverId);
    }

    public double getDriverRating(Long driverId){
        if(driverId == null){
            throw  new ResourceNotFound("DriverId is empty");
        }
        return ratingRepository.averageGradeByDriverId(driverId);
    }

    public Double addRating(RatingCreateDto ratingCreateDto,Driver driver){
        Rating rating= Rating.builder()
                .grade(ratingCreateDto.grade())
                .driver(driver)
                .passengerId(ratingCreateDto.passengerId())
                .build();
        ratingRepository.save(rating);
        Double grade=ratingRepository.averageGradeByDriverId(driver.getId());
        return grade;
    }
    
}
