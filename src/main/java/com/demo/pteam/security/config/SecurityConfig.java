package com.demo.pteam.security.config;

import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.configurer.ApiLoginConfigurer;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.login.handler.LoginAuthenticationFailureHandler;
import com.demo.pteam.security.login.handler.LoginAuthenticationSuccessHandler;
import com.demo.pteam.security.login.LoginAuthenticationProvider;
import com.demo.pteam.security.login.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final AccountService accountService;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = getAuthenticationManager(http);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .with(ApiLoginConfigurer.create(), config -> config
                        .loginProcessingUrl("/api/auths/login")
                        .successHandler(new LoginAuthenticationSuccessHandler(jwtProvider))
                        .failureHandler(new LoginAuthenticationFailureHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auths/login").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    private AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(getLoginAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    private AuthenticationProvider getLoginAuthenticationProvider() {
        LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider();
        loginAuthenticationProvider.setUserDetailsService(new CustomUserDetailsService(accountService));
        return loginAuthenticationProvider;
    }
}
