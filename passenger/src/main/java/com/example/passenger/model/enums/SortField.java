package com.example.passenger.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {

    ID("id"),
    EMAIL("email"),
    FIRST_NAME("firstName"),
    PHONE_NUMBER("phoneNumber"),
    GRADE("grade");

    private final String databaseFieldName;
}
