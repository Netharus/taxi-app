package com.example.passenger.controller;

import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponseDto createPassenger(@RequestBody PassengerCreateDto passengerCreateDto) {
        return passengerService.createPassenger(passengerCreateDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponseDto modifyPassenger(@RequestBody PassengerUpdateDto passengerUpdateDto) {
        return passengerService.updatePassenger(passengerUpdateDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponseDto getPassenger(@PathVariable Long id) {
        return passengerService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable Long id){
        passengerService.deletePassenger(id);
    }

}
