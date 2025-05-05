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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler;

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthenticationException {
        Authentication authResult = this.attemptAuthentication(request);
        if (authResult != null) {
            this.successfulAuthentication(request, response, authResult);
        }
        chain.doFilter(request, response);
    }

    private Authentication attemptAuthentication(HttpServletRequest request) {
        String accessToken = this.obtainAccessToken(request);
        String refreshToken = this.obtainRefreshToken(request);
        JwtToken token = new JwtToken(accessToken, refreshToken);
        if (token.isEmpty()) {  // 토큰이 없을 경우
            return null;
        }
        JwtAuthenticationToken authRequest = JwtAuthenticationToken.unauthenticated(token);
        return this.authenticationManager.authenticate(authRequest);
    }

    private String obtainAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private String obtainRefreshToken(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        successHandler.onAuthenticationSuccess(request, response, authentication);
    }
}
