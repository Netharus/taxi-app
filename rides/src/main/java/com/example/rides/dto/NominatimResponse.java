package com.example.rides.dto;

import lombok.Builder;

@Builder
public record NominatimResponse(
        String lat,
        String lon
) {
}
