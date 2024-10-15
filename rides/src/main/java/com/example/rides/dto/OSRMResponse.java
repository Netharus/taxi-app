package com.example.rides.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OSRMResponse(List<Route> routes) {
    public record Route(
            double distance,
            double duration
    ) {
    }
}
