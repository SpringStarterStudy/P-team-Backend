package com.demo.pteam.security.jwt;

import java.util.Optional;

public interface TokenStore {
    void save(Long accountId, String refreshToken, long expirationMillis);
    void delete(Long accountId);
    boolean isInvalid(Long accountId, String refreshToken);
    Optional<TokenData> findByAccountId(Long accountId);
}
