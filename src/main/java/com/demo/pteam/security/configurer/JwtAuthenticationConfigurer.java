package com.demo.pteam.security.configurer;

import com.demo.pteam.security.authentication.JwtAuthenticationFilter;
import com.demo.pteam.security.login.ApiLoginFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {
    private final JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter();
    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler;

    @Override
    public void configure(HttpSecurity http) {
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        http.setSharedObject(JwtAuthenticationFilter.class, authenticationFilter);
        http.addFilterBefore(authenticationFilter, ApiLoginFilter.class);
    }

    public JwtAuthenticationConfigurer authenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    public JwtAuthenticationConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }
}
