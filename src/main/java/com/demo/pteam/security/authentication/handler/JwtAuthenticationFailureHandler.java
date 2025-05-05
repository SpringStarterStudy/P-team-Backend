package com.demo.pteam.security.authentication.handler;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.exception.ExpiredTokenException;
import com.demo.pteam.security.exception.InvalidJwtException;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof DisabledException ||        // 계정 정지
                exception instanceof ExpiredTokenException ||       // 토큰 만료
                exception instanceof BadCredentialsException ||     // 사용자 정보 x
                exception instanceof InvalidJwtException) {         // 유효하지 않은 토큰
            writeAuthenticationFailureResponse(response, AuthenticationErrorCode.INVALID_AUTHENTICATION);
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
