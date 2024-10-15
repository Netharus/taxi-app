package com.example.rides.dto;

import java.util.List;

@BUilder
public record OSRMResponse(List<Route> routes) {
    public record Route(
            double distance,
            double duration
    ) {
    }
}
