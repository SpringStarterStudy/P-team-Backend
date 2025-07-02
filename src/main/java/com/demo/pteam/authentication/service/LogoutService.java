package com.demo.pteam.authentication.service;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.security.jwt.JwtService;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final JwtService jwtService;

    public void logout(String refreshToken) {
        try {
            jwtService.invalidateRefreshToken(refreshToken, "logout");
        } catch (IllegalArgumentException | JwtException e) {
            throw new ApiException(AuthenticationErrorCode.INVALID_AUTHENTICATION);
        }
    }
}
