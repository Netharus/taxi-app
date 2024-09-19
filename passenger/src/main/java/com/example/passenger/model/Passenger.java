package com.example.passenger.model;

import com.example.passenger.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "passenger")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "first_name")
    private String firstName;

    @Column(nullable = false,unique = true,name = "email")
    private String email;

    @Column(nullable = false,unique = true,name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role = Role.USER;

    @Column( name="grade")
    private Double grade;

    @OneToMany(mappedBy = "passenger")
    private List<Rating> ratingList;
}
