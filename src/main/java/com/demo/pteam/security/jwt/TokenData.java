package com.demo.pteam.security.jwt;

public record TokenData(
        String token,
        long expirationMillis
) {
    boolean isSameToken(String token) {
        return this.token.equals(token);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expirationMillis;
    }
}
