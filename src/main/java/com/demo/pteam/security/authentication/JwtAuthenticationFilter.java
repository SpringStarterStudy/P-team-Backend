package com.demo.pteam.security.authentication;

import com.demo.pteam.security.authentication.dto.JwtToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthenticationException {
        try {
            Authentication authResult = this.attemptAuthentication(request);
            if (authResult != null) {
                this.successfulAuthentication(request, response, authResult);
            }
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            this.failureHandler.onAuthenticationFailure(request, response, e);
        }
    }

    private Authentication attemptAuthentication(HttpServletRequest request) {
        String authHeader = this.obtainAuthHeader(request);
        String refreshHeader = this.obtainRefreshHeader(request);
        JwtToken token = JwtToken.ofBearer(authHeader, refreshHeader);
        if (token.isEmpty()) {  // 토큰이 없을 경우
            return null;
        }
        JwtAuthenticationToken authRequest = JwtAuthenticationToken.unauthenticated(token);
        return this.authenticationManager.authenticate(authRequest);
    }

    private String obtainAuthHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private String obtainRefreshHeader(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.successHandler.onAuthenticationSuccess(request, response, authentication);
    }
}
