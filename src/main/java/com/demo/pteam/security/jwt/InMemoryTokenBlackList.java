package com.demo.pteam.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InMemoryTokenBlackList implements TokenBlackList {
    private final ConcurrentHashMap<String, BlackListedToken> blackList = new ConcurrentHashMap<>();

    @Override
    public void save(String token, String reason, long expirationMillis) {
        BlackListedToken existingToken = blackList.putIfAbsent(
                token, new BlackListedToken(reason, expirationMillis));
        if (existingToken != null) {
            log.warn("This token is already blacklisted.");
        }
    }

    @Override
    public boolean isBlackListed(String token) {
        return blackList.containsKey(token);
    }

    @Scheduled(fixedRate = 3600000)     // 1시간 간격
    public void cleanExpiredTokens() {
        blackList.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}
