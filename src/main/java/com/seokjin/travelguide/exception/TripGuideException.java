package com.seokjin.travelguide.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class TripGuideException extends RuntimeException {
    public final Map<String, String> validation = new HashMap<>();

    public TripGuideException(String message) {
        super(message);
    }

    public TripGuideException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
