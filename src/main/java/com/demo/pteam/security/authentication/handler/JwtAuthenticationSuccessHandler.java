package com.demo.pteam.security.authentication.handler;

import com.demo.pteam.security.authentication.JwtAuthenticationToken;
import com.demo.pteam.security.authentication.dto.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Object credentials = authentication.getCredentials();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            jwtAuthentication.eraseToken();
        }
        if (credentials instanceof JwtToken token) {
            addJwtTokenToHeader(response, token);
        } else {
            throw new InternalAuthenticationServiceException("Unexpected credentials type");
        }
    }

    private void addJwtTokenToHeader(HttpServletResponse response, JwtToken token) {
        response.setHeader("Authorization", "Bearer " + token.extractAccessToken());
        response.setHeader("Refresh-Token", "Bearer " + token.extractRefreshToken());
    }
}
