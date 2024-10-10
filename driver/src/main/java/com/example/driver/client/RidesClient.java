package com.example.driver.client;

import com.example.driver.dto.DriverResponseForRideDto;
import com.example.driver.model.enums.Status;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ridesService", url = "http://localhost:8083/api/v1/rides")
public interface RidesClient {

    @RequestMapping(method = RequestMethod.POST, value = "/accept")
    void acceptRide(@RequestBody DriverResponseForRideDto driverResponseForRideDto, @RequestParam("rideId") Long rideId);

    @RequestMapping(method = RequestMethod.POST, value = "/decline")
    void declineRide(@RequestBody DriverResponseForRideDto driverResponseForRideDto, @RequestParam("rideId") Long rideId);

    @RequestMapping(method = RequestMethod.POST, value = "/change_status")
    void changeRideStatus(@RequestParam("status") Status status, @RequestBody DriverResponseForRideDto driverResponseForRideDto, @RequestParam("rideId") Long rideId);

    @RequestMapping(method = RequestMethod.POST, value = "/end")
    void endRide(@RequestBody DriverResponseForRideDto driverResponseForRideDto, @RequestParam("rideId") Long rideId);
}
