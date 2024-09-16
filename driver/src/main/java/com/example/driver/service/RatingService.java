package com.example.driver.service;

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

    public Rating saveRating(Rating rating){
        return ratingRepository.save(rating);
    }
    @Transactional
    public void deleteByDriver(Long driverId){
        ratingRepository.deleteAllByDriverId(driverId);
    }

    public List<Rating> getDriverWithRating(Double rating){
        return ratingRepository.findByGradeGreaterThanEqual(rating);
    }
    
}
