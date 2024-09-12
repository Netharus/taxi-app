package com.example.driver.validator;
import java.util.*;


import com.example.driver.dto.DriverCreateDto;

import java.util.Set;

public interface ObjectValidator<T> {
    public void validate(T o);

}
