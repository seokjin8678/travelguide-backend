package com.seokjin.travelguide.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class TripGuideException extends RuntimeException {
    public final Map<String, String> validation = new HashMap<>();
    private final String logMessage;

    public TripGuideException(String message, String logMessage) {
        super(message);
        this.logMessage = logMessage;
    }

    public TripGuideException(String message, Throwable cause, String logMessage) {
        super(message, cause);
        this.logMessage = logMessage;
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
