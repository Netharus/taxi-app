package com.example.driver.repository;

import com.example.driver.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findByDriverId(Long driverId);

    void deleteAllByDriverId(Long driverId);
}
