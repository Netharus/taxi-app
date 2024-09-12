package com.example.driver.controller;


import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
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
@RequestMapping("/api/driver")
public class DriverController {


    private final DriverService driverService;

    @PostMapping
    //TODO study statuses
    @ResponseStatus(HttpStatus.CREATED)
    public void createDriver(@RequestBody DriverCreateDto driverCreateDto){
        driverService.createDriver(driverCreateDto);
    }


    //TODO
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateDriver(@RequestBody DriverUpdateDto driverUpdateDto){
        driverService.updateDriver(driverUpdateDto);
    }

    @GetMapping
    public Page<DriverResponse> findAllByPage(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int sizePerPage,
                              @RequestParam(defaultValue = "ID") SortField sortField,
                              @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection){
        Pageable pageable=PageRequest.of(page, sizePerPage, sortDirection, sortField.getDatabaseFieldName());
        return driverService.findAllByPage(pageable);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@RequestParam Long driverId ){
        driverService.deleteDriver(driverId);
    }


}
