package com.seokjin.travelguide.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends TripGuideException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequestException(String logMessage) {
        super(MESSAGE, logMessage);
    }

    public InvalidRequestException(String logMessage, String fieldName, String message) {
        super(MESSAGE, logMessage);
        addValidation(fieldName, message);
    }

    public InvalidRequestException(String logMessage, Validation... validations) {
        super(MESSAGE, logMessage);
        for (Validation validation : validations) {
            addValidation(validation.getFieldName(), validation.getMessage());
        }
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
