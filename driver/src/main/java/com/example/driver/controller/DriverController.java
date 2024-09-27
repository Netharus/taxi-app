package com.example.driver.controller;

import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.dto.ContainerDriverResponse;
import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.dto.RatingCreateDto;
import com.example.driver.service.CarService;
import com.example.driver.service.DriverService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController {


    private final DriverService driverService;
    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse createDriver(@Valid @RequestBody DriverCreateDto driverCreateDto) {
        return driverService.createDriver(driverCreateDto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DriverResponse updateDriver(@Valid @RequestBody DriverUpdateDto driverUpdateDto, @PathVariable Long driverId) {
        return driverService.updateDriver(driverUpdateDto,driverId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ContainerDriverResponse findAllByPage(@PageableDefault(page = 0, size = 10,sort = "ID",direction = Sort.Direction.DESC) Pageable pageable,
                                                 @RequestParam(defaultValue = "") String keyword) {
        return driverService.findAllByPage(pageable, keyword);
    }

    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long driverId) {
        driverService.deleteDriver(driverId);
    }

    @PostMapping("/rating")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRating(@Valid @RequestBody RatingCreateDto ratingCreateDto) {
    driverService.addRating(ratingCreateDto);
    }

    @GetMapping("/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverResponse getDriver(@PathVariable Long driverId) {
        return driverService.getDriverById(driverId);
    }

    @PostMapping("/cars")
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponseDto addCar(@Valid @RequestBody CarStandaloneCreateDto carCreateDto) {
        return driverService.addCar(carCreateDto);
    }
    @DeleteMapping("/cars/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long carId) {
        carService.deleteById(carId);
    }
}
