package com.demo.pteam.security.authentication;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.exception.LoginErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        if (exception instanceof AuthorizationDeniedException) {    // 권한 x
            writeAuthenticationFailureResponse(response, LoginErrorCode.INVALID_ACCESS);
        } else {    // 서버 에러
            writeAuthenticationFailureResponse(response, LoginErrorCode.AUTHORIZATION_FAILED);
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
