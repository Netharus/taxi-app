package com.example.passenger.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ContainerPassengerResponseDto(
        List<PassengerResponseDto> content,
        Integer size,
        Integer pageNum,
        Long totalElements,
        Integer totalPages
) {
}
