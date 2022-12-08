package com.seokjin.travelguide.exception;

public class TripNotFoundException extends TripGuideException {

    private static final String MESSAGE = "존재하지 않는 여행입니다.";

    public TripNotFoundException(String logMessage) {
        super(MESSAGE, logMessage);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
