package com.seokjin.travelguide.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends TripGuideException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequestException() {
        super(MESSAGE);
    }

    public InvalidRequestException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public InvalidRequestException(Validation... validations) {
        super(MESSAGE);
        for (Validation validation : validations) {
            addValidation(validation.getFieldName(), validation.getMessage());
        }
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
