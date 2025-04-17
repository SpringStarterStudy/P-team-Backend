package com.demo.pteam.security.login.handler;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.exception.InvalidJsonFieldException;
import com.demo.pteam.security.exception.LoginErrorCode;
import com.demo.pteam.security.exception.MethodNotAllowedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof BadCredentialsException) {   // 로그인 정보 불일치
            ErrorCode errorCode = LoginErrorCode.INVALID_CREDENTIALS;
            writeLoginFailureResponse(response, errorCode, errorCode.getMessage());
        } else if (exception instanceof DisabledException) {    // 계정 정지
            ErrorCode errorCode = LoginErrorCode.INVALID_CREDENTIALS;
            writeLoginFailureResponse(response, errorCode, errorCode.getMessage());
        } else if (exception instanceof MethodNotAllowedException) {   // post 요청 x
            ErrorCode errorCode = LoginErrorCode.METHOD_NOT_ALLOWED;
            response.setHeader("Allow", HttpMethod.POST.name());
            writeLoginFailureResponse(response, errorCode, errorCode.getMessage());
        } else if (exception instanceof InvalidJsonFieldException e) {    // 잘못된 json 요청
            ErrorCode errorCode = LoginErrorCode.INVALID_FIELD;
            String message = String.format(errorCode.getMessage(), e.getPropertyName());
            writeLoginFailureResponse(response, errorCode, message);
        } else {
            ErrorCode errorCode = LoginErrorCode.LOGIN_FAILED;
            writeLoginFailureResponse(response, errorCode, errorCode.getMessage());
        }
    }

    private static void writeLoginFailureResponse(HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus().value());
        String body = objectMapper.writeValueAsString(ApiResponse.error(errorCode, message));
        response.getWriter().write(body);
    }
}
