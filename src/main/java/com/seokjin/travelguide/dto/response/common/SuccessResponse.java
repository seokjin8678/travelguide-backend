package com.seokjin.travelguide.dto.response.common;

import com.seokjin.travelguide.dto.response.Response;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SuccessResponse<T> extends Response {
    private T result;

    public SuccessResponse(String code, String message) {
        super(code, message);
    }

    @Builder
    public SuccessResponse(String code, String message, T result) {
        super(code, message);
        this.result = result;
    }
}
