package com.seokjin.travelguide.controller;

import com.seokjin.travelguide.domain.User;
import com.seokjin.travelguide.dto.request.SignUpRequest;
import com.seokjin.travelguide.dto.response.Response;
import com.seokjin.travelguide.dto.response.SignUpResponse;
import com.seokjin.travelguide.dto.response.SuccessResponse;
import com.seokjin.travelguide.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public String signin() {
        return "signin";
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody @Valid SignUpRequest request) {
        request.validate();
        User user = authService.signUp(request);
        SignUpResponse signUpResponse = SignUpResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
        return ResponseEntity.ok()
                .body(new SuccessResponse<>("200", "회원가입이 완료되었습니다.", signUpResponse));
    }
}
