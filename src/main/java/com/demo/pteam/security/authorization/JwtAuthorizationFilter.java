package com.demo.pteam.security.authorization;

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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthenticationException {
        Authentication authResult = this.attemptAuthentication(request);
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        this.securityContextHolderStrategy.setContext(context);
        chain.doFilter(request, response);
    }

    private Authentication attemptAuthentication(HttpServletRequest request) {
        String accessToken = this.obtainAccessToken(request);
        accessToken = accessToken != null ? accessToken.trim() : "";
        String refreshToken = this.obtainRefreshToken(request);
        refreshToken = refreshToken != null ? refreshToken.trim() : "";
        JwtAuthenticationToken authRequest = JwtAuthenticationToken.unauthenticated(new JwtToken(accessToken, refreshToken));
        return this.authenticationManager.authenticate(authRequest);
    }

    private String obtainAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private String obtainRefreshToken(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
