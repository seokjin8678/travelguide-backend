package com.seokjin.travelguide.exception;

import lombok.Getter;

@Getter
public class Validation {
    private final String fieldName;
    private final String message;

    private Validation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public static Validation of(String fieldName, String message) {
        return new Validation(fieldName, message);
    }
}
