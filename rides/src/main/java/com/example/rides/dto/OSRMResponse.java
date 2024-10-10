package com.example.rides.dto;

import java.util.List;

public record OSRMResponse(List<Route> routes) {
    public record Route(
            double distance,
            double duration
    ) {
    }
}
