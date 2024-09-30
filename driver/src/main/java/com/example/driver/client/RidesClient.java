package com.example.driver.client;

import com.example.driver.dto.DriverResponseForRideDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ridesService", url = "http://localhost:8083")
public interface RidesClient {

    @RequestMapping(method = RequestMethod.POST, value = "api/v1/rides/accept")
    void acceptRide(@RequestBody DriverResponseForRideDto driverResponseForRideDto,  @RequestParam Long rideId);
}
