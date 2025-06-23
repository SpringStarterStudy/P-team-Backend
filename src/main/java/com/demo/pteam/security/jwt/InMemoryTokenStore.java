package com.demo.pteam.security.jwt;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenStore implements TokenStore {
    private final ConcurrentHashMap<Long, TokenData> store = new ConcurrentHashMap<>();

    @Override
    public void save(Long accountId, String refreshToken, long expirationMillis) {
        store.put(accountId, new TokenData(refreshToken, expirationMillis));
    }

    @Override
    public void delete(Long accountId) {
        store.remove(accountId);
    }

    @Override
    public boolean isInvalid(Long accountId, String refreshToken) {
        TokenData data = store.get(accountId);
        return data == null ||
                !data.isSameToken(refreshToken) ||
                data.isExpired();
    }

    @Scheduled(fixedRate = 3600000)     // 1시간 간격
    public void cleanExpiredTokens() {
        store.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}
