package com.seokjin.travelguide.service.auth;

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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtTokenProvider implements InitializingBean {

    private final String secret;
    private final long tokenValidateMilliSecond;

    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.token-validate-second}") long tokenValidateSecond) {
        this.secret = secret;
        this.tokenValidateMilliSecond = tokenValidateSecond * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        claims.put("roles", roles);
        claims.put("nickname", userDetails.getNickname());
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
            log.info("잘못된 JWT 서명입니다. {}", token);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. {}", token);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다. {}", token);
        } catch (SignatureException e) {
            log.info("JWS 서명 확인이 실패했습니다. {}", token);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다. {}", token);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        CustomUser user = new CustomUser(claims.getSubject(), "", claims.get("nickname").toString(),
                authorities);

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }
}
