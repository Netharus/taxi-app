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
    PHONE_NUMBER("phoneNumber");
    //TODO how to sort by rating

    private final String databaseFieldName;
}