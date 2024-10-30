package com.example.driver.unit.service;

import com.example.driver.dto.RatingCreateDto;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import com.example.driver.model.enums.SortField;
import com.example.driver.repository.RatingRepository;
import com.example.driver.service.RatingService;
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

import static com.example.driver.util.UnitTestUtils.getDriverWithId;
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
        // Arrange
        Driver driver = getDriverWithId();
        RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                .driverId(1L)
                .grade(3)
                .passengerId(1L)
                .build();
        Rating rating = Rating.builder()
                .grade(ratingCreateDto.grade())
                .driver(driver)
                .passengerId(ratingCreateDto.passengerId())
                .build();
        SortField sortField = SortField.valueOf("ID");
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(0, 20, direction, sortField.getDatabaseFieldName());
        Page<Rating> page = new PageImpl<>(List.of(rating), pageable, 1);
        when(ratingRepository.findByDriverId(pageable, driver.getId())).thenReturn(page);
        doReturn(rating).when(ratingRepository).save(any(Rating.class)); // Используем any(Rating.class)
        double expectedGrade = 3.0;

        // Act
        double actualGrade = ratingService.addRating(ratingCreateDto, driver);

        // Assert
        assertEquals(expectedGrade, actualGrade);
        verify(ratingRepository).findByDriverId(pageable, driver.getId());
        verify(ratingRepository).save(any(Rating.class)); // Проверяем, что save был вызван
    }

}
