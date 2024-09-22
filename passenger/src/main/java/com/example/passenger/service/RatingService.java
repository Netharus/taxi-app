package com.example.passenger.service;

import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import com.example.passenger.model.enums.SortField;
import com.example.passenger.repository.RatingRepository;
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

    private  final RatingRepository ratingRepository;


    public Rating saveRating(Rating rating){
        return ratingRepository.save(rating);
    }
    @Transactional
    public void deleteByPassengerId(Long passengerId){
        ratingRepository.deleteAllByPassengerId(passengerId);
    }


    public Double addRating(RatingCreateDto ratingCreateDto, 
                            Passenger passenger){
        Rating rating= Rating.builder()
                .grade(ratingCreateDto.grade())
                .passenger(passenger)
                .driverId(ratingCreateDto.passengerId())
                .build();
        ratingRepository.save(rating);
        return averageGrade(getTenLastRating(passenger.getId()));
    }

    public Page<Rating> getTenLastRating(Long byId) {
        SortField sortField= SortField.valueOf("ID");
        Sort.Direction direction= Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(
                0,
                10,
                direction,
                sortField.getDatabaseFieldName());
        return ratingRepository.findByPassengerId(pageable,byId);
    }
    private Double averageGrade(Page<Rating> ratings){
        Double average=0.0;
        for(Rating rating:ratings){
            average+=rating.getGrade();
        }
        average/=ratings.getNumberOfElements();
        return average;
    }
}
