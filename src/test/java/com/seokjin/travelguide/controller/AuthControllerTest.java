package com.seokjin.travelguide.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seokjin.travelguide.domain.member.Member;
import com.seokjin.travelguide.dto.request.auth.SignInRequest;
import com.seokjin.travelguide.dto.request.auth.SignUpRequest;
import com.seokjin.travelguide.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("회원가입이 정상적으로 되어야 한다.")
    void signUpCouldBeSuccess() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setNickname("test");
        signUpRequest.setPassword("123456");
        signUpRequest.setConfirmPassword("123456");
        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .build();
        doReturn(member).when(authService)
                .signUp(any(SignUpRequest.class));

        // expect
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.email").value(signUpRequest.getEmail()))
                .andExpect(jsonPath("$.result.nickname").value(signUpRequest.getNickname()));
    }

    @Test
    @DisplayName("회원가입을 할 때 이메일이 이메일 형식이어야 한다.")
    void signUpByEmailCouldBeEmailFormat() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test");
        signUpRequest.setNickname("test");
        signUpRequest.setPassword("123456");
        signUpRequest.setConfirmPassword("123456");

        // expect
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.email").exists());
    }

    @Test
    @DisplayName("회원가입을 할 때 이메일의 길이는 30자리 이내여야 한다.")
    void signUpByNicknameLengthCouldBeMinTo30() throws Exception {
        // given
        String email = "12345678901234567890@123456.com"; // 31
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setNickname("nickname");
        signUpRequest.setPassword("123456");
        signUpRequest.setConfirmPassword("123456");

        // expect
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.email").exists());
    }

    @ParameterizedTest
    @DisplayName("회원가입을 할 때 닉네임의 길이는 2자리에서 10자리 사이여야 한다.")
    @ValueSource(strings = {"t", "1234567890a"})
    void signUpByEmailLengthCouldBe2To10(String nickname) throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setNickname(nickname);
        signUpRequest.setPassword("123456");
        signUpRequest.setConfirmPassword("123456");

        // expect
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.nickname").exists());
    }

    @ParameterizedTest
    @DisplayName("회원가입을 할 때 비밀번호의 길이는 6자리에서 20자리 사이여야 한다.")
    @ValueSource(strings = {"12345", "123456789012345678901"})
    void signUpByPasswordLengthCouldBe6To20(String password) throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setNickname("test");
        signUpRequest.setPassword(password);
        signUpRequest.setConfirmPassword(password);

        // expect
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.password").exists());
    }

    @Test
    @DisplayName("회원가입을 할 때 비밀번호와 확인 비밀번호는 같아야 한다.")
    void signUpByPasswordAndConfirmPasswordCouldBeSame() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setNickname("test");
        signUpRequest.setPassword("123456");
        signUpRequest.setConfirmPassword("abcdef");

        // expect
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.password").exists())
                .andExpect(jsonPath("$.validation.confirmPassword").exists());
    }

    @Test
    @DisplayName("로그인이 정상적으로 되어야 한다.")
    void signInCouldBeSuccess() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test@test.com");
        signInRequest.setPassword("123456");

        doReturn("jwttoken")
                .when(authService)
                .signIn(any(SignInRequest.class));

        // expect
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer jwttoken"))
                .andExpect(jsonPath("$.result").value("Bearer jwttoken"));
    }

    @Test
    @DisplayName("로그인을 할 때 이메일이 이메일 형식이어야 한다.")
    void signInByEmailCouldBeEmailFormat() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test");
        signInRequest.setPassword("123456");

        // expect
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.email").exists());
    }

    @Test
    @DisplayName("로그인을 할 때 이메일의 길이는 30자리 이내여야 한다.")
    void signInByEmailLengthCouldBeMinTo30() throws Exception {
        // given
        String email = "12345678901234567890@123456.com"; // 31
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail(email);
        signInRequest.setPassword("123456");

        // expect
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.email").exists());
    }

    @ParameterizedTest
    @DisplayName("로그인을 할 때 비밀번호의 길이는 6자리에서 20자리 사이여야 한다.")
    @ValueSource(strings = {"12345", "123456789012345678901"})
    void signInByPasswordLengthCouldBeMinTo30(String password) throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test@test.com");
        signInRequest.setPassword(password);

        // expect
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.password").exists());
    }
}