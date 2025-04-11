package com.demo.pteam.security.config;

import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.login.ApiLoginFilter;
import com.demo.pteam.security.login.LoginAuthenticationProvider;
import com.demo.pteam.security.login.CustomUserDetailsService;
import com.demo.pteam.security.login.LoginAuthenticationSuccessHandler;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/auths/login", "POST");

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
                .addFilterAt(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(DEFAULT_ANT_PATH_REQUEST_MATCHER).permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    private AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(getLoginAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    private AuthenticationProvider getLoginAuthenticationProvider() {
        LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider();
        loginAuthenticationProvider.setUserDetailsService(new CustomUserDetailsService(accountService));
        return loginAuthenticationProvider;
    }

    private Filter apiLoginFilter(AuthenticationManager authenticationManager) {
        ApiLoginFilter filter = new ApiLoginFilter(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(getLoginAuthenticationSuccessHandler());
        return filter;
    }

    private AuthenticationSuccessHandler getLoginAuthenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler(jwtProvider);
    }
}
