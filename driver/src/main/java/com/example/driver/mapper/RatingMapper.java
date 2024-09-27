package com.example.driver.mapper;

import com.example.driver.dto.RatingCreateDto;
import com.example.driver.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RatingMapper {

    Rating fromRatingCreateDto(RatingCreateDto ratingCreateDto);

}
