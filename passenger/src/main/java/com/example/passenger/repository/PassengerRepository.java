package com.example.passenger.repository;

import com.example.passenger.model.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    @Query("SELECT p from Passenger p WHERE p.phoneNumber like %?1%"
            + "or p.email like %?1%"
            + "or p.firstName like %?1%")
    Page<Passenger> findAllByPage(String Keyword, Pageable pageable);
}
