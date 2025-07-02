package com.demo.pteam.security.jwt;

public interface TokenBlackList {
    void save(String token, String reason, long expirationMillis);
    boolean isBlackListed(String token);
}
