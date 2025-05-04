package com.demo.pteam.security.config;

import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.authentication.JwtService;
import com.demo.pteam.security.authentication.JwtAuthenticationProvider;
import com.demo.pteam.security.authentication.JwtAuthenticationFilter;
import com.demo.pteam.security.authentication.JwtAuthenticationSuccessHandler;
import com.demo.pteam.security.configurer.ApiLoginConfigurer;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.login.handler.LoginAuthenticationFailureHandler;
import com.demo.pteam.security.login.handler.LoginAuthenticationSuccessHandler;
import com.demo.pteam.security.login.LoginAuthenticationProvider;
import com.demo.pteam.security.login.LoginUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final AccountService accountService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = getApiLoginAuthenticationManager();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .with(new ApiLoginConfigurer(objectMapper), config -> config
                        .authenticationManager(authenticationManager)
                        .loginProcessingUrl("/api/auths/login")
                        .successHandler(new LoginAuthenticationSuccessHandler(jwtProvider))
                        .failureHandler(new LoginAuthenticationFailureHandler())
                )
                .addFilterBefore(getJwtAuthenticationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)     // TODO: 나중에 configurer 사용하도록 변경
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auths/login").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    private AuthenticationManager getApiLoginAuthenticationManager() {
        return new ProviderManager(getJwtAuthenticationProvider(), getLoginAuthenticationProvider());
    }

    private AuthenticationProvider getLoginAuthenticationProvider() {
        LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider();
        loginAuthenticationProvider.setUserDetailsService(new LoginUserDetailsService(accountService));
        return loginAuthenticationProvider;
    }

    private JwtAuthenticationFilter getJwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setSuccessHandler(new JwtAuthenticationSuccessHandler());
        return authenticationFilter;
    }

    private JwtAuthenticationProvider getJwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtService);
    }
}
