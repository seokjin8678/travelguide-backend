package com.seokjin.travelguide.service;

import static org.junit.jupiter.api.Assertions.*;

import com.seokjin.travelguide.domain.Role;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

class JwtTokenProviderTest {
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secret = "123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef";
        jwtTokenProvider = new JwtTokenProvider(secret, 38800);
        jwtTokenProvider.afterPropertiesSet();
    }

    @Test
    void name() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                "test@test.com", "1234", List.of(new SimpleGrantedAuthority(Role.ROLE_MEMBER.name()),
                new SimpleGrantedAuthority(Role.ROLE_ADMIN.name())));
        String token = jwtTokenProvider.createToken(authenticationToken);
        System.out.println(token);
    }
}