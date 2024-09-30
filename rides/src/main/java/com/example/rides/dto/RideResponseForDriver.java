package com.example.rides.dto;

public record RideResponseForDriver(
        Long rideId,
        String startPoint,
        String endPoint,
        Double price
) {
}
