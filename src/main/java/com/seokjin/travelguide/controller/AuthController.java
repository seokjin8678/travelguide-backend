package com.seokjin.travelguide.controller;

import static com.seokjin.travelguide.config.JwtAuthorizationFilter.AUTHORIZATION_HEADER;

import com.seokjin.travelguide.domain.Member;
import com.seokjin.travelguide.dto.request.SignInRequest;
import com.seokjin.travelguide.dto.request.SignUpRequest;
import com.seokjin.travelguide.dto.response.Response;
import com.seokjin.travelguide.dto.response.SignUpResponse;
import com.seokjin.travelguide.dto.response.SuccessResponse;
import com.seokjin.travelguide.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<Response> signin(@RequestBody @Valid SignInRequest request, HttpServletRequest httpRequest) {
        String token = authService.signIn(request);
        log.info("{}님이 로그인 하였습니다. IP={}, TOKEN={}", request.getEmail(), httpRequest.getRemoteAddr(), token);
        return ResponseEntity.ok()
                .header(AUTHORIZATION_HEADER, "Bearer " + token)
                .body(new SuccessResponse<>("200", "로그인 성공", token));
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody @Valid SignUpRequest request, HttpServletRequest httpRequest) {
        request.validate();
        Member member = authService.signUp(request);
        SignUpResponse signUpResponse = SignUpResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
        log.info("(email:{}, nickname:{})님이 회원가입 하였습니다. IP={}", member.getEmail(), member.getNickname(),
                httpRequest.getRemoteAddr());
        return ResponseEntity.ok()
                .body(new SuccessResponse<>("200", "회원가입이 완료되었습니다.", signUpResponse));
    }
}
