package com.example.rides.repository;

import com.example.rides.model.Rides;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RidesRepository extends JpaRepository<Rides, Long> {
}
