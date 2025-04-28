package com.demo.pteam.security.login;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.exception.InvalidJsonPropertyException;
import com.demo.pteam.security.exception.MethodNotAllowedException;
import com.demo.pteam.security.dto.LoginRequest;
import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiLoginFilterTest {
    @InjectMocks
    private ApiLoginFilter apiLoginFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        apiLoginFilter.setAuthenticationManager(authenticationManager);
    }

    @DisplayName("로그인 요청")
    @Test
    void attemptAuthentication() throws IOException {
        // given
        String username = "username";
        String password = "1234567aA!";
        LoginRequest testLoginRequest = new LoginRequest(username, password);
        String requestBody = objectMapper.writeValueAsString(testLoginRequest);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(requestBody);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        Authentication testAuthRequest = getTestAuthentication(mockRequest, testLoginRequest);

        when(authenticationManager.authenticate(testAuthRequest)).thenReturn(getTestAuthenticated());

        // when
        Authentication authentication = apiLoginFilter.attemptAuthentication(mockRequest, mockResponse);

        // then
        assertThat(authentication).isNotNull();
    }

    @DisplayName("로그인 요청 - POST 메서드가 아닌 경우")
    @Test
    void attemptAuthentication_notPOST() throws JsonProcessingException {
        // given
        String username = "username";
        String password = "1234567aA!";
        LoginRequest testLoginRequest = new LoginRequest(username, password);
        String requestBody = objectMapper.writeValueAsString(testLoginRequest);
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(requestBody, HttpMethod.GET.name());
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        ThrowingCallable action = () -> apiLoginFilter.attemptAuthentication(mockRequest, mockResponse);

        // then
        assertThatExceptionOfType(MethodNotAllowedException.class)
                .isThrownBy(action)
                .withMessage("Authentication method not supported: " + mockRequest.getMethod());
    }

    @DisplayName("로그인 요청 - json 형식이 올바르지 않은 경우")
    @Test
    void attemptAuthentication_invalidJson() {
        // given
        String invalidJson = "invalid request body";
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(invalidJson);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        ThrowingCallable action = () -> apiLoginFilter.attemptAuthentication(mockRequest, mockResponse);

        // then
        assertThatThrownBy(action).isInstanceOf(AuthenticationServiceException.class);
    }

    @DisplayName("로그인 요청 - json property가 올바르지 않은 경우")
    @Test
    void attemptAuthentication_invalidJsonProperty() {
        // given
        String invalidJson = """
                {
                    "test": "a"
                }
                """;
        MockHttpServletRequest mockRequest = getMockHttpServletRequest(invalidJson);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        ThrowingCallable action = () -> apiLoginFilter.attemptAuthentication(mockRequest, mockResponse);

        // then
        assertThatThrownBy(action).isInstanceOf(InvalidJsonPropertyException.class);
    }

    private static MockHttpServletRequest getMockHttpServletRequest(String requestBody) {
        return getMockHttpServletRequest(requestBody, HttpMethod.POST.name());
    }

    private static MockHttpServletRequest getMockHttpServletRequest(String requestBody, String method) {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setMethod(method);
        mockRequest.setRequestURI("/api/auths/login");
        mockRequest.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mockRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        mockRequest.setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        return mockRequest;
    }

    private static Authentication getTestAuthentication(MockHttpServletRequest mockRequest, LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken testAuthRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        testAuthRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(mockRequest));
        return testAuthRequest;
    }

    private static UserPrincipal getTestPrincipal() {
        return new UserPrincipal(1L, Role.ROLE_USER, true);
    }

    private static UsernamePasswordAuthenticationToken getTestAuthenticated() {
        return UsernamePasswordAuthenticationToken.authenticated(
                getTestPrincipal(), "test123!", List.of(new SimpleGrantedAuthority(AccountStatus.ACTIVE.name())));
    }
}