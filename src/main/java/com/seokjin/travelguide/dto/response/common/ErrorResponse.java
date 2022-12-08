package com.seokjin.travelguide.dto.response.common;

import com.seokjin.travelguide.dto.response.Response;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * { "code": "400", "message": "잘못된 요청입니다.", "validation": { "title": "값을 입력해주세요" } }
 */
@Getter
public class ErrorResponse extends Response {
    public static ErrorResponse UNAUTHORIZED = new ErrorResponse("401", "권한이 없습니다.", null);
    public static ErrorResponse FORBIDDEN = new ErrorResponse("403", "권한이 없습니다.", null);

    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        super(code, message);
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
