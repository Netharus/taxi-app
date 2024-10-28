package com.example.passenger.unit.service;

import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import com.example.passenger.model.enums.SortField;
import com.example.passenger.repository.RatingRepository;
import com.example.passenger.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.example.passenger.unit.service.util.UnitTestUtils.getPassenger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    public void givenRatingCreateDtoAndDriver_whenAddRating_thenReturnDoubleGrade() {
        //Arrange
        Passenger passenger = getPassenger();
        RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                .driverId(1L)
                .grade(3)
                .passengerId(1L)
                .build();
        Rating rating = Rating.builder()
                .grade(ratingCreateDto.grade())
                .driverId(ratingCreateDto.driverId())
                .passenger(passenger)
                .build();
        SortField sortField = SortField.valueOf("ID");
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(0, 10, direction, sortField.getDatabaseFieldName());
        Page<Rating> page = new PageImpl<>(List.of(rating), pageable, 1);
        when(ratingRepository.findByPassengerId(pageable, passenger.getId())).thenReturn(page);
        doReturn(rating).when(ratingRepository).save(any(Rating.class));
        double expectedGrade = 3.0;

        // Act
        double actualGrade = ratingService.addRating(ratingCreateDto, passenger);

        // Assert
        assertEquals(expectedGrade, actualGrade);
        verify(ratingRepository).findByPassengerId(pageable, passenger.getId());
        verify(ratingRepository).save(any(Rating.class));
    }
}
