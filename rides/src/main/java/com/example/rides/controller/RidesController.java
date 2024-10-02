package com.example.rides.controller;

import com.example.rides.dto.DriverResponseForRideDto;
import com.example.rides.dto.RideCreateResponseDto;
import com.example.rides.dto.RidesCreateDto;
import com.example.rides.dto.RidesInformationResponseDto;
import com.example.rides.model.enums.Status;
import com.example.rides.service.RidesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RidesController {

    private final RidesService ridesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideCreateResponseDto addRide(@RequestBody RidesCreateDto ridesCreateDto) {
        return ridesService.addRide(ridesCreateDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RidesInformationResponseDto checkRidesInformation(@RequestParam String startPoint,
                                                             @RequestParam String endPoint) {
        return ridesService.checkPrice(startPoint, endPoint);
    }

    @PostMapping("/accept")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptRide(@RequestBody DriverResponseForRideDto driverResponseForRideDto, @RequestParam Long rideId) {
        ridesService.acceptRide(driverResponseForRideDto, rideId);
    }
    @PostMapping("/decline")
    @ResponseStatus(HttpStatus.OK)
    public void declineRide(@RequestBody DriverResponseForRideDto driverResponseForRideDto, @RequestParam Long rideId) {
        ridesService.declineRide(driverResponseForRideDto, rideId);
    }
    @PostMapping("/change_status")
    @ResponseStatus(HttpStatus.OK)
    public void changeRideStatus(@RequestParam Status status, @RequestBody DriverResponseForRideDto driverResponseForRideDto, @RequestParam Long rideId) {
        ridesService.changeStatus(status,driverResponseForRideDto, rideId);
    }

    @PostMapping("/end")
    @ResponseStatus(HttpStatus.OK)
    void endRide(@RequestBody DriverResponseForRideDto driverResponseForRideDto,  @RequestParam Long rideId){
        ridesService.endDrive(driverResponseForRideDto,rideId);
    }
}
