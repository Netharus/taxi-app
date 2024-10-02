package com.example.rides.dto;

public record RideResponseForDriver(
        Long rideId,
        Long passengerId,
        String startPoint,
        String endPoint,
        Double price
) {
}
