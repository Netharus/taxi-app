package com.example.driver.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "brand")
    private String brand;

    @Column(nullable = false, name="color")
    private String color;

    @Column(nullable = false,unique = true,name = "registration_number")
    private String registrationNumber;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
