package com.example.driver.controller;


import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.dto.RatingCreateDto;
import com.example.driver.model.Rating;
import com.example.driver.model.enums.SortField;
import com.example.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController {


    private final DriverService driverService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse createDriver(@RequestBody DriverCreateDto driverCreateDto) {
        return driverService.createDriver(driverCreateDto);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DriverResponse updateDriver(@RequestBody DriverUpdateDto driverUpdateDto) {
        return driverService.updateDriver(driverUpdateDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<DriverResponse> findAllByPage(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int sizePerPage,
                                              @RequestParam(defaultValue = "ID") SortField sortField,
                                              @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
                                              @RequestParam(defaultValue = "") String keyword,
                                              @RequestParam(defaultValue = "") Double rating) {
        Pageable pageable = PageRequest.of(page, sizePerPage, sortDirection, sortField.getDatabaseFieldName());
        return driverService.findAllByPage(pageable, keyword);
    }

    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long driverId) {
        driverService.deleteDriver(driverId);
    }

    @PostMapping("/rating")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRating(@RequestBody RatingCreateDto  ratingCreateDto) {
    driverService.addRating(ratingCreateDto);
    }

    @GetMapping("/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverResponse getDriver(@PathVariable Long driverId) {
        return driverService.getDriverById(driverId);
    }
//    @GetMapping()
//    @ResponseStatus(HttpStatus.OK)
//    public List<DriverResponse> getDriversByRating(@RequestParam Double rating) {
//        return driverService.findDriversByRating(rating);
//    }

}
