package com.example.driver.mapper;

import com.example.driver.dto.RatingCreateDto;
import com.example.driver.model.Rating;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-19T19:14:16+0300",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class RatingMapperImpl implements RatingMapper {

    @Override
    public Rating fromRatingCreateDto(RatingCreateDto ratingCreateDto) {
        if ( ratingCreateDto == null ) {
            return null;
        }

        Rating.RatingBuilder rating = Rating.builder();

        rating.grade( ratingCreateDto.grade() );
        rating.passengerId( ratingCreateDto.passengerId() );

        return rating.build();
    }
}
