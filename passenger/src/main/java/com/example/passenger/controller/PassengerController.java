package com.example.passenger.controller;

import com.example.passenger.dto.ContainerPassengerResponseDto;
import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.dto.RideCreateResponseDto;
import com.example.passenger.dto.RidesCreateDto;
import com.example.passenger.dto.RidesInformationResponseDto;
import com.example.passenger.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponseDto createPassenger(@Valid @RequestBody PassengerCreateDto passengerCreateDto) {
        return passengerService.createPassenger(passengerCreateDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponseDto modifyPassenger(@Valid @RequestBody PassengerUpdateDto passengerUpdateDto, @PathVariable Long id) {
        return passengerService.updatePassenger(passengerUpdateDto,id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponseDto getPassenger(@PathVariable Long id) {
        return passengerService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ContainerPassengerResponseDto findAllByPage(@PageableDefault(size = 10, sort = "ID", direction = Sort.Direction.DESC) Pageable pageable,
                                                       @RequestParam(defaultValue = "") String keyword) {
        return passengerService.findAllByPage(pageable, keyword);
    }

    @PostMapping("/rating")
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponseDto addRating(@Valid @RequestBody RatingCreateDto ratingCreateDto) {
        return passengerService.addRating(ratingCreateDto);
    }
    @GetMapping("/rides")
    @ResponseStatus(HttpStatus.OK)
    public RidesInformationResponseDto checkRidesInformation(@RequestParam String startPoint, @RequestParam String endPoint){
        return passengerService.getRideInformation(startPoint,endPoint);
    }
    @PostMapping("/rides")
    @ResponseStatus(HttpStatus.OK)
    public RideCreateResponseDto checkRidesInformation(@Valid @RequestBody RidesCreateDto ridesCreateDto){
        return passengerService.createRide(ridesCreateDto);
    }

    @PostMapping("/rides/notify")
    @ResponseStatus(HttpStatus.OK)
    public void notifyPassenger(@RequestBody RideCreateResponseDto rideCreateResponseDto){
        passengerService.notifyPassenger(rideCreateResponseDto);
    }

    @PostMapping("/rides/notify_end")
    @ResponseStatus(HttpStatus.OK)
    public void notifyEndRidePassenger(@RequestBody RideCreateResponseDto rideCreateResponseDto){
        passengerService.notifyAboutEnding(rideCreateResponseDto);
    }
}
