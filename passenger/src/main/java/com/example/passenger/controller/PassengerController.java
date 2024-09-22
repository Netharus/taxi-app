package com.example.passenger.controller;

import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.model.enums.SortField;
import com.example.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PassengerResponseDto> findAllByPage(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int sizePerPage,
                                              @RequestParam(defaultValue = "ID") SortField sortField,
                                              @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
                                              @RequestParam(defaultValue = "") String keyword) {
        Pageable pageable = PageRequest.of(page, sizePerPage, sortDirection, sortField.getDatabaseFieldName());
        return passengerService.findAllByPage(pageable, keyword);
    }
}
