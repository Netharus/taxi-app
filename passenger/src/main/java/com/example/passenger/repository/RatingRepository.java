package com.example.passenger.repository;

import com.example.passenger.model.Passenger;
import com.example.passenger.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    void deleteAllByPassengerId(Long id);

    Page<Rating> findByPassengerId(Pageable pageable, Long byId);
}
