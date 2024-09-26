package com.example.driver.dto;

import java.util.List;

public record ContainerDriverResponse(
        List<DriverResponse> driverResponses,
        Integer size,
        Integer pageNum,
        Long totalElements,
        Integer totalPages
) {
}
