package com.seokjin.travelguide.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

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
    @WithMockUser
    void withSignInCouldBeReturn200Status() throws Exception {
        // expect
        mockMvc.perform(post("/api/v1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("관리자 권한이 없으면 HTTP 403 상태 코드를 반환한다.")
    @WithMockUser(authorities = {"MEMBER"})
    void withoutAdminRoleCouldBeReturn403Status() throws Exception {
        // expect
        mockMvc.perform(post("/api/v1/adminTest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 권한이 있으면 HTTP 200 상태 코드를 반환한다.")
    @WithMockUser(authorities = {"ADMIN"})
    void withAdminAuthCouldBeReturn200Status() throws Exception {
        // expect
        mockMvc.perform(post("/api/v1/adminTest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}