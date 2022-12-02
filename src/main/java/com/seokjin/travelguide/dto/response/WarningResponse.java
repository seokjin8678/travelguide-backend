package com.seokjin.travelguide.dto.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;

public class WarningResponse<T> extends Response {
    private final Map<String, String> validation;
    private T result;

    @Builder
    public WarningResponse(String code, String message, Map<String, String> validation, T result) {
        super(code, message);
        this.validation = validation != null ? validation : new HashMap<>();
        this.result = result;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
