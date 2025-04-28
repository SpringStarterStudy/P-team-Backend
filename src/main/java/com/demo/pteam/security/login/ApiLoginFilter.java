package com.demo.pteam.security.login;

import com.demo.pteam.security.exception.InvalidJsonFormatException;
import com.demo.pteam.security.exception.InvalidJsonPropertyException;
import com.demo.pteam.security.exception.MethodNotAllowedException;
import com.demo.pteam.security.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
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
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/auths/login");

    private final ObjectMapper objectMapper;

    public ApiLoginFilter(ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new MethodNotAllowedException("Authentication method not supported: " + request.getMethod());
        } else {
            try {
                LoginRequest loginRequest = obtainLoginRequest(request);
                String username = this.obtainUsername(loginRequest);
                username = username != null ? username.trim() : "";
                String password = this.obtainPassword(loginRequest);
                password = password != null ? password : "";
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (UnrecognizedPropertyException e) {
                String propertyName = e.getPropertyName();
                throw new InvalidJsonPropertyException(e.getMessage(), e, propertyName);
            } catch (JsonProcessingException e) {
                throw new InvalidJsonFormatException(e.getMessage(), e);
            } catch (IOException e) {
                throw new AuthenticationServiceException(e.getMessage(), e);
            }
        }
    }

    private LoginRequest obtainLoginRequest(HttpServletRequest request) throws IOException {
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
