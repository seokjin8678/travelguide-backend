package com.seokjin.travelguide.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@PropertySource("classpath:jwt.properties")
public class JwtTokenProvider implements InitializingBean {

    private final String secret;
    private final long tokenValidateMilliSecond;

    private Key key;

    public JwtTokenProvider(@Value("${secret}") String secret,
                            @Value("${token-validate-second}") long tokenValidateSecond) {
        this.secret = secret;
        this.tokenValidateMilliSecond = tokenValidateSecond * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidateMilliSecond))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다. = {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. = {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다. = {}", e.getMessage());
        } catch (SignatureException e) {
            log.info("JWS 서명 확인이 실패했습니다. = {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다. = {}", e.getMessage());
        }
        return false;
    }
}
