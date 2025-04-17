package com.demo.pteam.security.login.handler;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationSuccessHandlerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;

    @Mock
    JwtProvider jwtProvider;

    @DisplayName("로그인 응답")
    @Test
    void onAuthenticationSuccess() throws IOException {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        UserPrincipal testPrincipal = getTestPrincipal();
        Authentication testAuthentication = getTestAuthentication(testPrincipal);
        String testAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDQ4OTI1NzgsImV4cCI6MTc0NDg5NjE3OH0.j5z_RoIBqYKCXiOlelrLtAk--4RUHy_qaVe1yzQxztQ";
        String testRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ0ODkyNTc4LCJleHAiOjE3NDU0OTczNzh9.ZZ-AB4LA5xwOvToTGaMSDMzMIWpPgDTKP0HjfDNMlPE";
        when(jwtProvider.generateAccessToken(testPrincipal)).thenReturn(testAccessToken);
        when(jwtProvider.generateRefreshToken(testPrincipal)).thenReturn(testRefreshToken);

        // when
        loginAuthenticationSuccessHandler.onAuthenticationSuccess(mockRequest, mockResponse, testAuthentication);

        // then
        assertThat(mockResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(mockResponse.getHeader("Authorization")).isEqualTo("Bearer " + testAccessToken);
        assertThat(mockResponse.getHeader("Refresh-Token")).isEqualTo("Bearer " + testRefreshToken);
        assertThat(mockResponse.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
        assertThat(mockResponse.getContentType()).contains(MediaType.APPLICATION_JSON_VALUE);
        String expectedBody = objectMapper.writeValueAsString(ApiResponse.success("로그인 성공"));
        assertThat(mockResponse.getContentAsString()).isEqualTo(expectedBody);
    }

    @DisplayName("로그인 응답 - principal 타입 캐스팅 실패")
    @Test
    void onAuthenticationSuccess_objectTypeMismatch() {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        String UnexpectedType = "test";
        Authentication testAuthentication = getTestAuthentication(UnexpectedType);

        // when
        ThrowingCallable action = () -> loginAuthenticationSuccessHandler.onAuthenticationSuccess(mockRequest, mockResponse, testAuthentication);

        // then
        assertThatExceptionOfType(InternalAuthenticationServiceException.class)
                .isThrownBy(action)
                .withMessage("Unexpected principal type");
    }

    private static UserPrincipal getTestPrincipal() {
        return new UserPrincipal(1L, Role.ROLE_USER, true);
    }

    private static Authentication getTestAuthentication(Object testPrincipal) {
        String testCredentials = "test123!";
        List<SimpleGrantedAuthority> testAuthorities = List.of(new SimpleGrantedAuthority(AccountStatus.ACTIVE.name()));
        return UsernamePasswordAuthenticationToken.authenticated(testPrincipal, testCredentials, testAuthorities);
    }
}