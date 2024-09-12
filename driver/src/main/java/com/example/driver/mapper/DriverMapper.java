package com.example.driver.mapper;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.model.Driver;
import com.example.driver.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverMapper {
    @Mapping(target = "rating", source = "rating")
    DriverResponse toDriverResponse(Driver driver,Double rating);

    List<DriverResponse> toDriverResponseList(List<Driver> drivers);

    Driver fromDriverRequest(DriverCreateDto driverCreateDto);
    @Mapping(target = "id", source = "id")
    Driver fromDriverUpdate(DriverUpdateDto driverUpdateDto);
}
