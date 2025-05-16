package com.demo.pteam.security.authentication;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.authentication.dto.JwtToken;
import com.demo.pteam.security.exception.ExpiredTokenException;
import com.demo.pteam.security.exception.InvalidJwtException;
import com.demo.pteam.security.principal.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.M2IjaJJCfnV7Eheijp72nKtVlL1pgkghNr-Zc1i6Oks";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.NXnMg9s2NpZFIgf6EmRdGpC9qyXDOWGbRklF39vOLBg";
    private static final String AUTHORIZATION_HEADER = PREFIX + ACCESS_TOKEN;
    private static final String REFRESH_TOKEN_HEADER = PREFIX + REFRESH_TOKEN;

    private static final String EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3MjcyMDc2OTksImV4cCI6MTcyNzIxMTI5OX0.K0j4PNjduPIor-Y2aGzgwAMNIncKMrtKtc2itmg2qP0";
    private static final String EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzI3MzExOTYyLCJleHAiOjE3Mjc5MTY3NjJ9.M7C2KBVQOU1X2g6TGT1VTPRAOv3BWLYC4LB0tO-xNfA";
    private static final String EXPIRED_AUTHORIZATION_HEADER = PREFIX + EXPIRED_ACCESS_TOKEN;
    private static final String EXPIRED_REFRESH_TOKEN_HEADER = PREFIX + EXPIRED_REFRESH_TOKEN;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationSuccessHandler successHandler;

    @Mock
    private AuthenticationFailureHandler failureHandler;

    @Mock
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
    }

    private static MockHttpServletRequest getMockHttpServletRequest(JwtToken token) {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setMethod(HttpMethod.GET.name());
        mockRequest.setRequestURI("/api/members");  // 사용자 인증이 필요한 경로
        mockRequest.addHeader("Authorization", token.getAuthHeader());
        mockRequest.addHeader("Refresh-Token", token.getRefreshHeader());
        mockRequest.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mockRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return mockRequest;
    }

    private static UserPrincipal getExpectedPrincipal() {
        return new UserPrincipal(1L, Role.ROLE_USER, true);
    }

    private static List<SimpleGrantedAuthority> getExpectedAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
    }

    @DisplayName("인증 성공")
    @Test
    void doFilterInternal() throws ServletException, IOException {
        // given
        JwtToken testToken = JwtToken.ofBearer(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(testToken);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        JwtAuthenticationToken unauthenticatedToken = new JwtAuthenticationToken(null, testToken);
        when(authenticationManager.authenticate(unauthenticatedToken))
                .thenReturn(new JwtAuthenticationToken(getExpectedPrincipal(), testToken, getExpectedAuthorities()));

        // when
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, chain);

        // then
        verify(successHandler).onAuthenticationSuccess(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any(Authentication.class));
        verify(chain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @DisplayName("인증 실패 - header가 빈 경우")
    @Test
    void doFilterInternal_headerIsEmpty() throws ServletException, IOException {
        // given
        JwtToken invalidToken = JwtToken.ofBearer(null, null);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(invalidToken);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, chain);

        // then
        verify(successHandler, never()).onAuthenticationSuccess(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any(Authentication.class));
        verify(chain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @DisplayName("인증 실패 - 모든 토큰 만료")
    @Test
    void doFilterInternal_expiredAllToken() throws ServletException, IOException {
        // given
        JwtToken expiredToken = JwtToken.ofBearer(EXPIRED_AUTHORIZATION_HEADER, EXPIRED_REFRESH_TOKEN_HEADER);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(expiredToken);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        JwtAuthenticationToken unauthenticatedToken = new JwtAuthenticationToken(null, expiredToken);
        when(authenticationManager.authenticate(unauthenticatedToken))
                .thenThrow(new ExpiredTokenException("Expired JWT token"));

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, chain);

        // then
        verify(chain, never()).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
        verify(failureHandler).onAuthenticationFailure(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any(ExpiredTokenException.class));
    }

    @DisplayName("인증 실패 - 유효하지 않은 서명")
    @ParameterizedTest
    @CsvSource({
            // 유효하지 않은 서명의 accessToken
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.YAlicbyqBqJMSfOYs1qH0hqOjPaXC6jHIdqKeB6dON8,"+REFRESH_TOKEN_HEADER,
            // 유효하지 않은 서명의 accessToken, refreshToken
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.YAlicbyqBqJMSfOYs1qH0hqOjPaXC6jHIdqKeB6dON8,Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.eR2aFv_159TMilurSHSIV-3yuvE1Dpv5VbEVVSNC1NI"
    })
    void doFilterInternal_invalidSignatureToken(String invalidAuthorizationHeader, String invalidRefreshTokenHeader) throws ServletException, IOException {
        // given
        JwtToken invalidToken = JwtToken.ofBearer(invalidAuthorizationHeader, invalidRefreshTokenHeader);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(invalidToken);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        JwtAuthenticationToken unauthenticatedToken = new JwtAuthenticationToken(null, invalidToken);
        when(authenticationManager.authenticate(unauthenticatedToken))
                .thenThrow(new InvalidJwtException("Invalid JWT token"));

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, chain);

        // then
        verify(chain, never()).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
        verify(failureHandler).onAuthenticationFailure(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any(InvalidJwtException.class));
    }

    @DisplayName("인증 실패 - 정지된 계정")
    @Test
    void doFilterInternal_accountIsSuspended() throws ServletException, IOException {
        // given
        JwtToken testToken = JwtToken.ofBearer(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(testToken);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        JwtAuthenticationToken unauthenticatedToken = new JwtAuthenticationToken(null, testToken);
        when(authenticationManager.authenticate(unauthenticatedToken))
                .thenThrow(new DisabledException("Disabled"));

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, chain);

        // then
        verify(chain, never()).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
        verify(failureHandler).onAuthenticationFailure(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any(DisabledException.class));
    }

    @DisplayName("인증 실패 - 사용자 조회 실패")
    @Test
    void doFilterInternal_usernameNotFound() throws ServletException, IOException {
        // given
        JwtToken testToken = JwtToken.ofBearer(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(testToken);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        JwtAuthenticationToken unauthenticatedToken = new JwtAuthenticationToken(null, testToken);
        when(authenticationManager.authenticate(unauthenticatedToken))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, chain);

        // then
        verify(chain, never()).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
        verify(failureHandler).onAuthenticationFailure(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any(BadCredentialsException.class));
    }
}