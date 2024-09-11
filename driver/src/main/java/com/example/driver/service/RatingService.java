package com.example.driver.service;

import com.example.driver.model.Rating;
import com.example.driver.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public Rating saveRating(Rating rating){
        return ratingRepository.save(rating);
    }

}
