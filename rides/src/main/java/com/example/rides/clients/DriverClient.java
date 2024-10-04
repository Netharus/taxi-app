package com.example.rides.clients;

import com.example.rides.dto.RideResponseForDriver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "driverService", url = "http://localhost:8081")
public interface DriverClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/drivers/rides/notify")
    void notifyDriver(@RequestBody RideResponseForDriver rideResponseForDriver);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/drivers/rides/notify_end")
    void notifyAboutEndDriver(@RequestBody RideResponseForDriver rideResponseForDriver);
}