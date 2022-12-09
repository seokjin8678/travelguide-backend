package com.seokjin.travelguide.dto.response.common;

import com.seokjin.travelguide.dto.response.Response;

public class SimpleResponse extends Response {

    private SimpleResponse(String code, String message) {
        super(code, message);
    }

    public static Response of(String code, String message) {
        return new SimpleResponse(code, message);
    }
}
