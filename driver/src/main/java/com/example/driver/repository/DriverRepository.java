package com.example.driver.repository;

import com.example.driver.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("SELECT d from Driver d WHERE d.email like %?1%"
            + "or d.fullName like %?1%"
            + "or d.phoneNumber like%?1%"
            + "or d.username like %?1%")
    public Page<Driver> findAll(String keyword, Pageable pageable);
}
