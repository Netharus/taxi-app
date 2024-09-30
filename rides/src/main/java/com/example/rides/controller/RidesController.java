package com.example.rides.controller;

import com.example.rides.dto.RideCreateResponseDto;
import com.example.rides.dto.RidesCreateDto;
import com.example.rides.dto.RidesInformationResponseDto;
import com.example.rides.model.Rides;
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
}
