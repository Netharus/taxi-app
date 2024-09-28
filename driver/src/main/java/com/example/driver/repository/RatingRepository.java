package com.example.driver.repository;

import com.example.driver.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    void deleteAllByDriverId(Long driverId);

    @Query("SELECT avg (r.grade) from Rating r where r.driver.id=?1")
    double averageGradeByDriverId(Long driverId);

    List<Rating> findLast10ByDriverId(Long driverId);

    Page<Rating> findByDriverId(Pageable pageable1, Long byId);
}
