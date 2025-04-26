package com.demo.pteam.security.authorization;

public record JwtToken(
        String accessToken,
        String refreshToken
) {
}
