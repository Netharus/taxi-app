package com.example.passenger.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @Column(nullable = false, name = "grade")
    private Integer grade;

    @Column(name = "driver_id")
    private Long driverId;

}
