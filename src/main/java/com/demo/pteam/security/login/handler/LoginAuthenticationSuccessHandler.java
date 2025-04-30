package com.demo.pteam.security.login.handler;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private ObjectMapper objectMapper;

    public LoginAuthenticationSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            String accessToken = jwtProvider.generateAccessToken(userPrincipal);
            String refreshToken = jwtProvider.generateRefreshToken(userPrincipal);
            writeLoginSuccessResponse(response, accessToken, refreshToken);
        } else {
            throw new InternalAuthenticationServiceException("Unexpected principal type");
        }
    }

    private void writeLoginSuccessResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", "Bearer " + refreshToken);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String body = objectMapper.writeValueAsString(ApiResponse.success("로그인 성공"));
        response.getWriter().write(body);
    }
}
