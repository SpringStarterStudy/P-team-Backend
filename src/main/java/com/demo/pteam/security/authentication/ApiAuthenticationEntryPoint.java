package com.demo.pteam.security.authentication;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public ApiAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof InsufficientAuthenticationException) {     // 인증 x
            writeAuthenticationFailureResponse(response, AuthenticationErrorCode.NOT_AUTHENTICATED);
        } else {    // 서버 에러
            writeAuthenticationFailureResponse(response, AuthenticationErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private void writeAuthenticationFailureResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus().value());
        String body = objectMapper.writeValueAsString(ApiResponse.error(errorCode, errorCode.getMessage()));
        response.getWriter().write(body);
    }
}
