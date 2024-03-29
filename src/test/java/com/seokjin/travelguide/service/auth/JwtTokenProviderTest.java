package com.seokjin.travelguide.service.auth;

import static org.assertj.core.api.Assertions.*;

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
        CustomUser user = new CustomUser("test@test.com", "123456",
                "nickname", List.of(new SimpleGrantedAuthority("MEMBER")));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());

        // when
        String token = jwtTokenProvider.createToken(authenticationToken);

        // then
        assertThat(jwtTokenProvider.validateToken(token))
                .isTrue();
        assertThat(jwtTokenProvider.getAuthentication(token).isAuthenticated())
                .isTrue();
    }
}