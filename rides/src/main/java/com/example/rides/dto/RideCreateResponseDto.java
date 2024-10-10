package com.example.rides.dto;

import com.example.rides.model.enums.Status;

public record RideCreateResponseDto(
        Long rideId,
        DriverResponseForRideDto driverInformation,
        String startPoint,
        String endPoint,
        Status status,
        Double price
) {
}
