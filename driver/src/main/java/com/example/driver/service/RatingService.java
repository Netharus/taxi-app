package com.example.driver.service;

import com.example.driver.dto.RatingCreateDto;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.model.enums.SortField;
import com.example.driver.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;


    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Transactional
    public void deleteByDriver(Long driverId) {
        ratingRepository.deleteAllByDriverId(driverId);
    }

    public Double addRating(RatingCreateDto ratingCreateDto, Driver driver) {
        Rating rating = Rating.builder()
                .grade(ratingCreateDto.grade())
                .driver(driver)
                .passengerId(ratingCreateDto.passengerId())
                .build();
        ratingRepository.save(rating);
        return averageGrade(getTenLastRating(driver.getId()));
    }

    public Page<Rating> getTenLastRating(Long byId) {
        SortField sortField = SortField.valueOf("ID");
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(0, 10, direction, sortField.getDatabaseFieldName());
        return ratingRepository.findByDriverId(pageable, byId);
    }

    public Double averageGrade(Page<Rating> ratings) {
        Double average = 0.0;
        for (Rating rating : ratings) {
            average += rating.getGrade();
        }
        average /= ratings.getNumberOfElements();
        return average;
    }
}
