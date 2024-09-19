package com.example.driver.mapper;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.model.Driver;
import com.example.driver.model.enums.Gender;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-19T19:14:16+0300",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class DriverMapperImpl implements DriverMapper {

    @Override
    public DriverResponse toDriverResponse(Driver driver) {
        if ( driver == null ) {
            return null;
        }

        Long id = null;
        Double grade = null;
        String username = null;
        String email = null;
        String fullName = null;
        String phoneNumber = null;
        Gender gender = null;

        id = driver.getId();
        grade = driver.getGrade();
        username = driver.getUsername();
        email = driver.getEmail();
        fullName = driver.getFullName();
        phoneNumber = driver.getPhoneNumber();
        gender = driver.getGender();

        DriverResponse driverResponse = new DriverResponse( id, grade, username, email, fullName, phoneNumber, gender );

        return driverResponse;
    }

    @Override
    public List<DriverResponse> toDriverResponseList(List<Driver> drivers) {
        if ( drivers == null ) {
            return null;
        }

        List<DriverResponse> list = new ArrayList<DriverResponse>( drivers.size() );
        for ( Driver driver : drivers ) {
            list.add( toDriverResponse( driver ) );
        }

        return list;
    }

    @Override
    public Driver fromDriverRequest(DriverCreateDto driverCreateDto) {
        if ( driverCreateDto == null ) {
            return null;
        }

        Driver.DriverBuilder driver = Driver.builder();

        driver.username( driverCreateDto.username() );
        driver.email( driverCreateDto.email() );
        driver.fullName( driverCreateDto.fullName() );
        driver.phoneNumber( driverCreateDto.phoneNumber() );
        if ( driverCreateDto.gender() != null ) {
            driver.gender( Enum.valueOf( Gender.class, driverCreateDto.gender() ) );
        }

        return driver.build();
    }

    @Override
    public Driver fromDriverUpdate(DriverUpdateDto driverUpdateDto) {
        if ( driverUpdateDto == null ) {
            return null;
        }

        Driver.DriverBuilder driver = Driver.builder();

        driver.id( driverUpdateDto.id() );
        driver.username( driverUpdateDto.username() );
        driver.email( driverUpdateDto.email() );
        driver.fullName( driverUpdateDto.fullName() );
        driver.phoneNumber( driverUpdateDto.phoneNumber() );
        if ( driverUpdateDto.gender() != null ) {
            driver.gender( Enum.valueOf( Gender.class, driverUpdateDto.gender() ) );
        }

        return driver.build();
    }
}
