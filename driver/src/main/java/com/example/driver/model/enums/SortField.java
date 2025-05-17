package com.example.driver.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
    ID("id"),
    USERNAME("username"),
    EMAIL("email"),
    FULL_NAME("fullName"),
    PHONE_NUMBER("phoneNumber"),
    GENDER("gender");

    private final String databaseFieldName;
}