package com.demo.pteam.security.configurer;

import com.demo.pteam.security.authentication.JwtAuthenticationFilter;
import com.demo.pteam.security.authentication.handler.JwtAuthenticationFailureHandler;
import com.demo.pteam.security.login.ApiLoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {
    private final ObjectMapper objectMapper;
    private final JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter();
    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    public JwtAuthenticationConfigurer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(HttpSecurity http) {
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        http.setSharedObject(JwtAuthenticationFilter.class, authenticationFilter);
        http.addFilterBefore(authenticationFilter, ApiLoginFilter.class);

        if (failureHandler instanceof JwtAuthenticationFailureHandler jwtFailureHandler) {
            jwtFailureHandler.setObjectMapper(objectMapper);
        }
    }

    public JwtAuthenticationConfigurer authenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    public JwtAuthenticationConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public JwtAuthenticationConfigurer failureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }
}
