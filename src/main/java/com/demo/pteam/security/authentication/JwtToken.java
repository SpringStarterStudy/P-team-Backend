package com.demo.pteam.security.authentication;

public record JwtToken(
        String accessToken,
        String refreshToken
) {
}
