package com.example.driver.dto;

import lombok.Builder;

@Builder
public record RideResponseForDriver(
        Long rideId,
        Long passengerId,
        String startPoint,
        String endPoint,
        Double price
) {
}
