package com.demo.pteam.security.jwt;

import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public JwtProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String generateAccessToken(UserPrincipal principal) {
        String sub = String.valueOf(principal.id());
        Map<String, Object> claims = objectMapper.convertValue(principal, new TypeReference<>() {});
        return JwtUtils.encode(sub, claims, secretKey, accessTokenExpiration);
    }

    public String generateRefreshToken(UserPrincipal principal) {
        String sub = String.valueOf(principal.id());
        return JwtUtils.encode(sub, secretKey, refreshTokenExpiration);
    }

    public boolean isExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration == null || expiration.before(new Date());
    }

    public Claims parseClaims(String token) throws JwtException {
        return JwtUtils.decode(token, secretKey);
    }
}
