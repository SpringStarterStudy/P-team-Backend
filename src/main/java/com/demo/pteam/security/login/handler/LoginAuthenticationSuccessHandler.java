package com.demo.pteam.security.login.handler;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.jwt.JwtService;
import com.demo.pteam.security.authentication.dto.JwtToken;
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
    private final JwtService jwtService;
    private ObjectMapper objectMapper;

    public LoginAuthenticationSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            JwtToken jwtToken = jwtService.createJwtToken(userPrincipal);
            writeLoginSuccessResponse(response, jwtToken);
        } else {
            throw new InternalAuthenticationServiceException("Unexpected principal type");
        }
    }

    private void writeLoginSuccessResponse(HttpServletResponse response, JwtToken jwtToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", jwtToken.getAuthHeader());
        response.setHeader("Refresh-Token", jwtToken.getRefreshHeader());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String body = objectMapper.writeValueAsString(ApiResponse.success("로그인 성공"));
        response.getWriter().write(body);
    }
}
