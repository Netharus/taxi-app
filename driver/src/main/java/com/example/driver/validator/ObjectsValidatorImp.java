package com.example.driver.validator;

import com.example.driver.dto.DriverCreateDto;
import com.example.driver.exceptions.ObjectNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectsValidatorImp<T> implements ObjectValidator<T> {
    private final ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Override
    public void validate(T o){
        Set<ConstraintViolation<T>> violations = validator.validate(o);
        if(!violations.isEmpty()){
            var errorMessages= violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            throw new ObjectNotValidException(errorMessages);
        }

    }


}
