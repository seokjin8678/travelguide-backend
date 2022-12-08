package com.seokjin.travelguide.exception;

public class MemberNotFoundException extends TripGuideException{

    private static final String MESSAGE = "존재하지 않는 유저입니다.";

    public MemberNotFoundException(String logMessage) {
        super(MESSAGE, logMessage);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
