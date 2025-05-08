package com.demo.pteam.security.authentication.dto;

import org.springframework.lang.NonNull;

public record JwtToken(
        @NonNull String accessToken,
        @NonNull String refreshToken
) {
    private static final String PREFIX = "Bearer ";

    public JwtToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken != null ? accessToken.trim() : "";
        this.refreshToken = refreshToken != null ? refreshToken.trim() : "";
    }

    public boolean isEmpty() {
        return accessToken.isEmpty() && refreshToken.isEmpty();
    }

    public boolean hasPrefix() {
        return accessToken.startsWith(PREFIX) && refreshToken.startsWith(PREFIX);
    }

    public String extractAccessToken() {
        return removePrefix(accessToken);
    }

    public String extractRefreshToken() {
        return removePrefix(refreshToken);
    }

    private String removePrefix(String token) {
        return token.startsWith(PREFIX) ? token.substring(PREFIX.length()) : token;
    }

    public JwtToken removePrefix() {
        return new JwtToken(extractAccessToken(), extractRefreshToken());
    }
}
