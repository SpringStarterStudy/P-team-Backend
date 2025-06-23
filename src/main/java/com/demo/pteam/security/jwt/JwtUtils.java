package com.demo.pteam.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    public static String encode(String subject, Map<String, Object> claims, String secretKey, Long expirationMillis, Date now) {
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(createSigningKey(secretKey))
                .compact();
    }

    public static String encode(String subject, String secretKey, Long expirationMillis, Date now) {
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(createSigningKey(secretKey))
                .compact();
    }

    public static Claims decode(String token, String secretKey) throws JwtException {
        return Jwts.parser()
                .verifyWith(createSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static SecretKey createSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
