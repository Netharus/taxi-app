package com.example.driver.model;

import com.example.driver.model.enums.Gender;
import com.example.driver.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true, name = "username")
    private String username;

    @Column(length = 50, nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, unique = true, name = "phone_number")
    private String phoneNumber;

    @Builder.Default
    @OneToMany(mappedBy = "driver")
    private List<Rating> ratingList = new ArrayList<>();

    @Column(name = "grade")
    private Double grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "gender")
    private Gender gender;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role = Role.USER;

    @Builder.Default
    @OneToMany(mappedBy = "driver")
    private List<Car> carList = new ArrayList<>();
}
