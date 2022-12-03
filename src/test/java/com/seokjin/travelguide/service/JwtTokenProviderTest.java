package com.seokjin.travelguide.service;

import static org.assertj.core.api.Assertions.*;

import com.seokjin.travelguide.domain.Role;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtTokenProviderTest {
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secret = "123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef";
        jwtTokenProvider = new JwtTokenProvider(secret, 38800);
        jwtTokenProvider.afterPropertiesSet();
    }

    @Test
    @DisplayName("JWT 토큰이 정상적으로 생성되어야 한다.")
    void createJwtTokenCouldBeSuccess() {
        // given
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                "test@test.com", "1234", List.of(new SimpleGrantedAuthority(Role.MEMBER.name())));
        // when
        String token = jwtTokenProvider.createToken(authenticationToken);

        // then
        assertThat(jwtTokenProvider.validateToken(token))
                .isTrue();
        assertThat(jwtTokenProvider.getAuthentication(token).isAuthenticated())
                .isTrue();
    }
}