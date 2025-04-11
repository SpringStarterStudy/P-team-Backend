package com.demo.pteam.security.jwt;

import com.demo.pteam.security.principal.PrincipalFactory;
import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public UserPrincipal getPrincipal(String token) throws JwtException {
        Claims claims = JwtUtils.decode(token, secretKey);
        return PrincipalFactory.fromClaims(claims);
    }
}
