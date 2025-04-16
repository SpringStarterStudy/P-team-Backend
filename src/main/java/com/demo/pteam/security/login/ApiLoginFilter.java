package com.demo.pteam.security.login;

import com.demo.pteam.security.exception.InvalidJsonFieldException;
import com.demo.pteam.security.exception.MethodNotAllowedException;
import com.demo.pteam.security.login.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/auths/login");

    public ApiLoginFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new MethodNotAllowedException("Authentication method not supported: " + request.getMethod());
        } else {
            try {
                LoginRequest loginRequest = getLoginRequest(request);
                String username = this.obtainUsername(loginRequest);
                username = username != null ? username.trim() : "";
                String password = this.obtainPassword(loginRequest);
                password = password != null ? password : "";
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new InvalidJsonFieldException(e.getMessage(), e);
            }
        }
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), LoginRequest.class);
    }

    @Nullable
    private String obtainPassword(LoginRequest request) {
        return request.password();
    }

    @Nullable
    private String obtainUsername(LoginRequest request) {
        return request.username();
    }

    private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
