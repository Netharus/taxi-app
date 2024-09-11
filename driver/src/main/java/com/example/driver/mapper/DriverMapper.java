package com.example.driver.mapper;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverMapper {
    DriverResponse toDriverResponse(Driver driver);

    List<DriverResponse> toDriverResponseList(List<Driver> drivers);

    Driver fromDriverRequest(DriverCreateDto driverCreateDto);

    Driver fromDriverUpdate(DriverUpdateDto driverUpdateDto);
}
