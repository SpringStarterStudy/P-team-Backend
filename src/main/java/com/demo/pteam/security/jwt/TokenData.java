package com.demo.pteam.security.jwt;

public record TokenData(
        String token,
        long expirationMillis
) {
    public boolean isSameToken(String token) {
        return this.token.equals(token);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationMillis;
    }
}
