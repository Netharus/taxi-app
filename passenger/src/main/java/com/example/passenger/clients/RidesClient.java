package com.example.passenger.clients;

import com.example.passenger.dto.RideCreateResponseDto;
import com.example.passenger.dto.RidesCreateDto;
import com.example.passenger.dto.RidesInformationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ridesService", url = "http://localhost:8083")
public interface RidesClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/rides")
    RidesInformationResponseDto checkRidesInformation(@RequestParam String startPoint,
                                                      @RequestParam String endPoint);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/rides")
    RideCreateResponseDto createRide(@RequestBody RidesCreateDto ridesCreateDto);
}
