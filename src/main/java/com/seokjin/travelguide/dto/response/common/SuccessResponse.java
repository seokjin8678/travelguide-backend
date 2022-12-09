package com.seokjin.travelguide.dto.response.common;

import com.seokjin.travelguide.dto.response.Response;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SuccessResponse<T> extends Response {
    private final T result;

    @Builder
    public SuccessResponse(String code, String message, T result) {
        super(code, message);
        this.result = result;
    }
}
