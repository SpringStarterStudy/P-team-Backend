package com.demo.pteam.security.configurer;

import com.demo.pteam.security.login.ApiLoginFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class ApiLoginConfigurer extends AbstractHttpConfigurer<ApiLoginConfigurer, HttpSecurity> {
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private ApiLoginFilter authenticationFilter;

    public ApiLoginConfigurer() {
        this.authenticationFilter = new ApiLoginFilter();
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        http.setSharedObject(ApiLoginFilter.class, authenticationFilter);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
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
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public static ApiLoginConfigurer create() {
        return new ApiLoginConfigurer();
    }
}
