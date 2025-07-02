package com.demo.pteam.security.jwt;

public record BlackListedToken(
        String reason,
        long expirationMillis
) {
    public boolean isExpired() {
        return System.currentTimeMillis() > expirationMillis;
    }
}
