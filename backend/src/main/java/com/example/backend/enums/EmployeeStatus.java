package com.example.backend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EmployeeStatus {
    ACTIVE("ACTIVE"),
    ON_LEAVE("ON LEAVE"),
    SICK_LEAVE("SICK LEAVE"),
    TERMINATED("TERMINATED");

    private final String value;

    EmployeeStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EmployeeStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(EmployeeStatus.values())
                .filter(status -> status.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are " + Stream.of(values()).map(EmployeeStatus::getValue).collect(Collectors.joining(","))));
    }
}
