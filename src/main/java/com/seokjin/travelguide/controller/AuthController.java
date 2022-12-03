package com.seokjin.travelguide.controller;

import static com.seokjin.travelguide.config.JwtAuthorizationFilter.AUTHORIZATION_HEADER;

import com.seokjin.travelguide.domain.Member;
import com.seokjin.travelguide.dto.request.SignInRequest;
import com.seokjin.travelguide.dto.request.SignUpRequest;
import com.seokjin.travelguide.dto.response.Response;
import com.seokjin.travelguide.dto.response.SignUpResponse;
import com.seokjin.travelguide.dto.response.SuccessResponse;
import com.seokjin.travelguide.service.AuthService;
import com.seokjin.travelguide.service.JwtTokenProvider;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signin")
    public ResponseEntity<Response> signin(@RequestBody @Valid SignInRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);
        log.info("{}님이 로그인 하였습니다.", request.getEmail());
        return ResponseEntity.ok()
                .header(AUTHORIZATION_HEADER, "Bearer " + token)
                .body(new SuccessResponse<>("200", "로그인 성공", token));
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody @Valid SignUpRequest request) {
        request.validate();
        Member member = authService.signUp(request);
        SignUpResponse signUpResponse = SignUpResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
        log.info("(email:{}, nickname:{})님이 회원가입 하였습니다.", member.getEmail(), member.getNickname());
        return ResponseEntity.ok()
                .body(new SuccessResponse<>("200", "회원가입이 완료되었습니다.", signUpResponse));
    }
}
