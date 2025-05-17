package com.example.driver.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ContainerDriverResponse(
        List<DriverResponse> driverResponses,
        Integer size,
        Integer pageNum,
        Long totalElements,
        Integer totalPages
) {
}
