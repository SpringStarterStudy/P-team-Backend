package com.demo.pteam.security.configurer;

import com.demo.pteam.security.login.ApiLoginFilter;
import com.demo.pteam.security.login.handler.LoginAuthenticationFailureHandler;
import com.demo.pteam.security.login.handler.LoginAuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class ApiLoginConfigurer extends AbstractHttpConfigurer<ApiLoginConfigurer, HttpSecurity> {
    private final ObjectMapper objectMapper;
    private final ApiLoginFilter authenticationFilter;
    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    public ApiLoginConfigurer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.authenticationFilter = new ApiLoginFilter(objectMapper);
    }

    @Override
    public void configure(HttpSecurity http) {
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        http.setSharedObject(ApiLoginFilter.class, authenticationFilter);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        if (successHandler instanceof LoginAuthenticationSuccessHandler loginSuccessHandler) {
            loginSuccessHandler.setObjectMapper(objectMapper);
        }
        if (failureHandler instanceof LoginAuthenticationFailureHandler loginFailureHandler) {
            loginFailureHandler.setObjectMapper(objectMapper);
        }
    }

    public ApiLoginConfigurer authenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    public ApiLoginConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public ApiLoginConfigurer failureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public ApiLoginConfigurer loginProcessingUrl(String loginProcessingUrl) {
        this.authenticationFilter.setRequiresAuthenticationRequestMatcher(this.createLoginProcessingUrlMatcher(loginProcessingUrl));
        return this;
    }

    private RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl);
    }
}
