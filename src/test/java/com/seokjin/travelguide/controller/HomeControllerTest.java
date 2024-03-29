package com.seokjin.travelguide.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seokjin.travelguide.WithMockCustomUser;
import com.seokjin.travelguide.config.CustomAccessDeniedHandler;
import com.seokjin.travelguide.config.CustomAuthenticationEntryPoint;
import com.seokjin.travelguide.config.SecurityConfig;
import com.seokjin.travelguide.service.auth.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import({HomeController.class, SecurityConfig.class})
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @SpyBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @SpyBean
    CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("로그인하지 않으면 HTTP 401 상태 코드를 반환한다.")
    void withoutSignInCouldBeReturn401Status() throws Exception {
        // expect
        mockMvc.perform(post("/api/v1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인하면 HTTP 200 상태 코드를 반환한다.")
    @WithMockCustomUser
    void withSignInCouldBeReturn200Status() throws Exception {
        // expect
        mockMvc.perform(post("/api/v1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("관리자 권한이 없으면 HTTP 403 상태 코드를 반환한다.")
    @WithMockCustomUser
    void withoutAdminRoleCouldBeReturn403Status() throws Exception {
        // expect
        mockMvc.perform(post("/api/v1/adminTest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 권한이 있으면 HTTP 200 상태 코드를 반환한다.")
    @WithMockCustomUser(roles = {"ADMIN"})
    void withAdminAuthCouldBeReturn200Status() throws Exception {
        // expect
        mockMvc.perform(post("/api/v1/adminTest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}