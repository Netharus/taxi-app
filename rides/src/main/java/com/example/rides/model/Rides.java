package com.example.rides.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.rides.model.enums.Status;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rides")
public class Rides {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(nullable = false, name = "passenger_id")
    private Long passengerId;

    @Column(nullable = false, name = "start_point")
    private String startPoint;

    @Column(nullable = false, name = "end_point")
    private String endPoint;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private Status status=Status.CREATED;

    @Builder.Default
    @Column(nullable = false, name = "order_date_time")
    private LocalDateTime orderDateTime=LocalDateTime.now();

    @Column(nullable = false, name = "price")
    private Double price;
}
