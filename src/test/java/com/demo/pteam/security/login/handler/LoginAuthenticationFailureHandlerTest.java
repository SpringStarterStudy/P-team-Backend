package com.demo.pteam.security.login.handler;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.exception.InvalidJsonPropertyException;
import com.demo.pteam.security.exception.LoginErrorCode;
import com.demo.pteam.security.exception.MethodNotAllowedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationFailureHandlerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;

    @DisplayName("계정 정보 불일치")   // 비밀번호 불일치, username 패턴 불일치, password 패턴 불일치, 존재하지 않는 계정
    @Test
    void onAuthenticationFailure_mismatch() throws IOException {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        BadCredentialsException testException = new BadCredentialsException("Bad credentials");

        // when
        loginAuthenticationFailureHandler.onAuthenticationFailure(mockRequest, mockResponse, testException);

        // then
        ErrorCode expectedErrorCode = LoginErrorCode.INVALID_CREDENTIALS;
        assertThat(mockResponse.getStatus()).isEqualTo(expectedErrorCode.getStatus().value());
        assertThat(mockResponse.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
        assertThat(mockResponse.getContentType()).contains(MediaType.APPLICATION_JSON_VALUE);
        String expectedBody = objectMapper.writeValueAsString(ApiResponse.error(expectedErrorCode, expectedErrorCode.getMessage()));
        assertThat(mockResponse.getContentAsString()).isEqualTo(expectedBody);
    }

    @DisplayName("계정 정지")
    @Test
    void onAuthenticationFailure_suspended() throws IOException {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        DisabledException testException = new DisabledException("Account is disabled");

        // when
        loginAuthenticationFailureHandler.onAuthenticationFailure(mockRequest, mockResponse, testException);

        // then
        ErrorCode expectedErrorCode = LoginErrorCode.ACCOUNT_SUSPENDED;
        assertThat(mockResponse.getStatus()).isEqualTo(expectedErrorCode.getStatus().value());
        assertThat(mockResponse.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
        assertThat(mockResponse.getContentType()).contains(MediaType.APPLICATION_JSON_VALUE);
        String expectedBody = objectMapper.writeValueAsString(ApiResponse.error(expectedErrorCode, expectedErrorCode.getMessage()));
        assertThat(mockResponse.getContentAsString()).isEqualTo(expectedBody);
    }

    @DisplayName("post 요청 x")
    @Test
    void onAuthenticationFailure_methodNotAllowed() throws IOException {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MethodNotAllowedException testException =
                new MethodNotAllowedException("Authentication method not supported: " + mockRequest.getMethod());

        // when
        loginAuthenticationFailureHandler.onAuthenticationFailure(mockRequest, mockResponse, testException);

        // then
        ErrorCode expectedErrorCode = LoginErrorCode.METHOD_NOT_ALLOWED;
        assertThat(mockResponse.getStatus()).isEqualTo(expectedErrorCode.getStatus().value());
        assertThat(mockResponse.getHeader("Allow")).isEqualTo(HttpMethod.POST.name());
        assertThat(mockResponse.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
        assertThat(mockResponse.getContentType()).contains(MediaType.APPLICATION_JSON_VALUE);
        String expectedBody = objectMapper.writeValueAsString(ApiResponse.error(expectedErrorCode, expectedErrorCode.getMessage()));
        assertThat(mockResponse.getContentAsString()).isEqualTo(expectedBody);
    }

    @DisplayName("잘못된 json property")
    @Test
    void onAuthenticationFailure_invalidJsonProperty() throws IOException {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        InvalidJsonPropertyException testException = new InvalidJsonPropertyException("invalid property", new IOException(), "invalidProperty");

        // when
        loginAuthenticationFailureHandler.onAuthenticationFailure(mockRequest, mockResponse, testException);

        // then
        ErrorCode expectedErrorCode = LoginErrorCode.INVALID_JSON_PROPERTY;
        assertThat(mockResponse.getStatus()).isEqualTo(expectedErrorCode.getStatus().value());
        assertThat(mockResponse.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
        assertThat(mockResponse.getContentType()).contains(MediaType.APPLICATION_JSON_VALUE);
        String expectedMessage = String.format(expectedErrorCode.getMessage(), testException.getPropertyName());
        String expectedBody = objectMapper.writeValueAsString(ApiResponse.error(expectedErrorCode, expectedMessage));
        assertThat(mockResponse.getContentAsString()).isEqualTo(expectedBody);
    }

    @DisplayName("서버 에러")
    @Test
    void onAuthenticationFailure_serverError() throws IOException {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        AuthenticationException testException = new AuthenticationServiceException("Server Error");

        // when
        loginAuthenticationFailureHandler.onAuthenticationFailure(mockRequest, mockResponse, testException);

        // then
        ErrorCode expectedErrorCode = LoginErrorCode.LOGIN_FAILED;
        assertThat(mockResponse.getStatus()).isEqualTo(expectedErrorCode.getStatus().value());
        assertThat(mockResponse.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
        assertThat(mockResponse.getContentType()).contains(MediaType.APPLICATION_JSON_VALUE);
        String expectedBody = objectMapper.writeValueAsString(ApiResponse.error(expectedErrorCode, expectedErrorCode.getMessage()));
        assertThat(mockResponse.getContentAsString()).isEqualTo(expectedBody);
    }
}