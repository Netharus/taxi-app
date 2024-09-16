package com.example.driver.repository;

import com.example.driver.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    void deleteAllByDriverId(Long driverId);

    List<Rating> findByGradeGreaterThanEqual(Double garde);
}
