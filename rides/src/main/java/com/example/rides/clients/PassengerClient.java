package com.example.rides.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "passengerService", url = "http://localhost:8082")
public interface PassengerClient {
}
