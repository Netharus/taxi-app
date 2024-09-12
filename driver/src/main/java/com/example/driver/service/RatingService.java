package com.example.driver.service;

import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public Rating saveRating(Rating rating){
        Rating savedRating=ratingRepository.save(rating);
        return savedRating;
    }
    @Transactional
    public void deleteByDriver(Long driverId){
        ratingRepository.deleteAllByDriverId(driverId);
    }
}
