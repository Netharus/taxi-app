package com.example.passenger.dto;

import java.util.List;

public record ContainerResponseDto(
        List<PassengerResponseDto> content,
        Integer size,
        Integer pageNum,
        Long totalElements,
        Integer totalPages

) {
}
