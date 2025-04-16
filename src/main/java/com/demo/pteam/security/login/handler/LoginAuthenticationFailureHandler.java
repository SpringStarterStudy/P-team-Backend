package com.demo.pteam.security.login.handler;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.global.exception.GlobalErrorCode;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.exception.InvalidJsonFieldException;
import com.demo.pteam.security.exception.LoginErrorCode;
import com.demo.pteam.security.exception.MethodNotAllowedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {   // 로그인 실패
            writeLoginFailureResponse(response, LoginErrorCode.INVALID_CREDENTIALS);
        } else if (exception instanceof DisabledException) {    // 계정 정지
            writeLoginFailureResponse(response, LoginErrorCode.ACCOUNT_SUSPENDED);
        } else if (exception instanceof MethodNotAllowedException) {   // post 메서드 x
            response.setHeader("Allow", HttpMethod.POST.name());
            writeLoginFailureResponse(response, LoginErrorCode.METHOD_NOT_ALLOWED);
        } else if (exception instanceof InvalidJsonFieldException) {    // 잘못된 json 요청
            writeLoginFailureResponse(response, GlobalErrorCode.VALIDATION_EXCEPTION);
        } else {
            writeLoginFailureResponse(response, LoginErrorCode.LOGIN_FAILED); // 로그인 에러
        }
    }

    private static void writeLoginFailureResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String body = objectMapper.writeValueAsString(ApiResponse.error(errorCode, errorCode.getMessage()));
        response.getWriter().write(body);
    }
}
