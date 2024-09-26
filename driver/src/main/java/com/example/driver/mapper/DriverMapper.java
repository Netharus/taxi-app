package com.example.driver.mapper;

import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.ContainerDriverResponse;
import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverMapper {
    @Mapping(target = "carResponseDto", source = "carResponseDto")
    DriverResponse toDriverResponse(Driver driver, List<CarResponseDto> carResponseDto);

    List<DriverResponse> toDriverResponseList(List<Driver> drivers);

    Driver fromDriverRequest(DriverCreateDto driverCreateDto);

    @Mapping(target = "id", source = "id")
    Driver fromDriverUpdate(DriverUpdateDto driverUpdateDto);

    @Mapping(target = "pageNum", source = "number")
    ContainerDriverResponse toContainerDriverResponse(Page<DriverResponse> driverResponses);
}
