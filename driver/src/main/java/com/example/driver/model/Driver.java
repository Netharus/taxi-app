package com.example.driver.model;

import com.example.driver.model.enums.Gender;
import com.example.driver.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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

    //TODO separate field
    //TODO probably I can separate only name, and save fullName field
    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, unique = true, name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "driver")
    private List<Rating> ratingList;

    @Column( name="grade")
    private Double grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name= "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role = Role.USER;

}
