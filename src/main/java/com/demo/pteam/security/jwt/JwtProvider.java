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

    @Value("${jwt.access-token-ttl}")
    private long accessTokenTTL;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTTL;

    public JwtProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String generateAccessToken(UserPrincipal principal, Date now) {
        String sub = String.valueOf(principal.id());
        Map<String, Object> claims = objectMapper.convertValue(principal, new TypeReference<>() {});
        return JwtUtils.encode(sub, claims, secretKey, accessTokenTTL, now);
    }

    public String generateRefreshToken(UserPrincipal principal, Date now) {
        String sub = String.valueOf(principal.id());
        return JwtUtils.encode(sub, secretKey, refreshTokenTTL, now);
    }

    public Claims parseClaims(String token) throws JwtException {
        return JwtUtils.decode(token, secretKey);
    }
}
