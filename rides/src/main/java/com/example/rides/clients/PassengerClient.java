package com.example.rides.clients;

import com.example.rides.dto.RideCreateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "passengerService", url = "http://localhost:8082/api/v1/passengers")
public interface PassengerClient {
    @RequestMapping(method = RequestMethod.POST, value = "rides/notify")
    void notifyPassenger(@RequestBody RideCreateResponseDto rideCreateResponseDto);

    @RequestMapping(method = RequestMethod.POST, value = "rides/notify_end")
    void notifyAboutEndPassenger(@RequestBody RideCreateResponseDto rideCreateResponseDto);
}
