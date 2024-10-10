package com.example.passenger.dto;


import com.example.passenger.model.enums.Status;

public record RideCreateResponseDto(
        Long rideId,
        DriverResponseForRideDto driverInformation,
        String startPoint,
        String endPoint,
        Status status,
        Double price
) {
}
