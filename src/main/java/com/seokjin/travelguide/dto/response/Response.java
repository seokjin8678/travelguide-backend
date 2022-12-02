package com.seokjin.travelguide.dto.response;

import lombok.Getter;

@Getter
public abstract class Response {
    private final String code;
    private final String message;

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
