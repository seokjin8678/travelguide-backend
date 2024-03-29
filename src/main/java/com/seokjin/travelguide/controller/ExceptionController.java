package com.seokjin.travelguide.controller;

import com.seokjin.travelguide.dto.response.common.ErrorResponse;
import com.seokjin.travelguide.dto.response.Response;
import com.seokjin.travelguide.exception.TripGuideException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> invalidRequestHandler(MethodArgumentNotValidException e,
                                                          HttpServletRequest request) {
        log.info("{}에 대한 요청이 실패했습니다. (검증 오류) IP={}", request.getRequestURI(), request.getRemoteAddr());
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(400)
                .body(response);
    }

    @ResponseBody
    @ExceptionHandler(TripGuideException.class)
    public ResponseEntity<Response> tripGuideException(TripGuideException e, HttpServletRequest request) {
        log.info("{}에 대한 요청이 실패했습니다. ({}) IP={}", request.getRequestURI(), e.getLogMessage(),
                request.getRemoteAddr());
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode)
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> badCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        log.info("{}에 대한 요청이 실패했습니다. (로그인 오류) IP={}", request.getRequestURI(), request.getRemoteAddr());
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("해당되는 계정이 없거나 비밀번호가 맞지 않습니다.")
                .build();
        response.addValidation("email", "해당되는 계정이 없거나 비밀번호가 맞지 않습니다.");
        response.addValidation("password", "해당되는 계정이 없거나 비밀번호가 맞지 않습니다.");
        return ResponseEntity.status(400)
                .body(response);
    }
}
