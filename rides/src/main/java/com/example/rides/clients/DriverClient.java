package com.example.rides.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "driverService", url = "http://localhost:8081")
public interface DriverClient {
}
