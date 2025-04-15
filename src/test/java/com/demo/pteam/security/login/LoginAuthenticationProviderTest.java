package com.demo.pteam.security.login;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.login.dto.LoginAccountInfo;
import com.demo.pteam.security.principal.CustomUserDetails;
import com.demo.pteam.security.principal.PrincipalFactory;
import com.demo.pteam.security.principal.UserPrincipal;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationProviderTest {
    @InjectMocks
    private LoginAuthenticationProvider loginAuthenticationProvider;

    @Mock
    private UserDetailsService userDetailsService;

    private final static PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "1234567aA!";

    private LoginAccountInfo getTestLoginAccountInfo() {
        return getTestLoginAccountInfo(AccountStatus.ACTIVE);
    }

    private LoginAccountInfo getTestLoginAccountInfo(AccountStatus status) {
        return new LoginAccountInfo(
                1L,
                USERNAME,
                passwordEncoder.encode(PASSWORD),
                status,
                Role.ROLE_USER
        );
    }

    private CustomUserDetails getTestUserDetails(LoginAccountInfo accountInfo) {
        return new CustomUserDetails(accountInfo, List.of(new SimpleGrantedAuthority(accountInfo.role().name())));
    }

    private UserPrincipal getTestPrincipal(CustomUserDetails userDetails) {
        return PrincipalFactory.fromUser(userDetails);
    }

    @DisplayName("로그인 인증")
    @Test
    void authenticate_success() {
        // given
        LoginAccountInfo testAccountInfo = getTestLoginAccountInfo();
        CustomUserDetails testUserDetails = getTestUserDetails(testAccountInfo);
        UserPrincipal testPrincipal = getTestPrincipal(testUserDetails);
        String requestUsername = "username";
        String requestPassword = "1234567aA!";
        when(userDetailsService.loadUserByUsername(requestUsername)).thenReturn(testUserDetails);
        Authentication testAuthentication = UsernamePasswordAuthenticationToken.unauthenticated(requestUsername, requestPassword);

        // when
        Authentication authentication = loginAuthenticationProvider.authenticate(testAuthentication);

        // then
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getPrincipal()).isEqualTo(testPrincipal);
        assertThat(authentication.getCredentials()).isEqualTo(testAuthentication.getCredentials());
        assertThat(authentication.getAuthorities()).isEqualTo(testUserDetails.getAuthorities());
    }

    @DisplayName("로그인 인증 - 비밀번호 불일치")
    @Test
    void authenticate_passwordMismatch() {
        // given
        LoginAccountInfo testAccountInfo = getTestLoginAccountInfo();
        CustomUserDetails testUserDetails = getTestUserDetails(testAccountInfo);
        String requestUsername = "username";
        String requestPassword = "12345aA!";
        when(userDetailsService.loadUserByUsername(requestUsername)).thenReturn(testUserDetails);
        Authentication testAuthentication = UsernamePasswordAuthenticationToken.unauthenticated(requestUsername, requestPassword);

        // when
        ThrowingCallable action = () -> loginAuthenticationProvider.authenticate(testAuthentication);

        // then
        assertThatThrownBy(action).isInstanceOf(BadCredentialsException.class);
    }

    @DisplayName("로그인 인증 - 존재하지 않는 계정")
    @Test
    void authenticate_notExist() {
        // given
        String requestUsername = "notExist";
        String requestPassword = "1234567aA!";
        when(userDetailsService.loadUserByUsername(requestUsername))
                .thenThrow(new UsernameNotFoundException("User not found: " + requestUsername));
        Authentication testAuthentication = UsernamePasswordAuthenticationToken.unauthenticated(requestUsername, requestPassword);

        // when
        ThrowingCallable action = () -> loginAuthenticationProvider.authenticate(testAuthentication);

        // then
        assertThatThrownBy(action).isInstanceOf(BadCredentialsException.class);
    }

    @DisplayName("로그인 인증 - 정지된 계정")
    @Test
    void authenticate_suspended() {
        // given
        LoginAccountInfo testAccountInfo = getTestLoginAccountInfo(AccountStatus.SUSPENDED);
        CustomUserDetails testUserDetails = getTestUserDetails(testAccountInfo);
        String requestUsername = "username";
        String requestPassword = "1234567aA!";
        when(userDetailsService.loadUserByUsername(requestUsername)).thenReturn(testUserDetails);
        Authentication testAuthentication = UsernamePasswordAuthenticationToken.unauthenticated(requestUsername, requestPassword);

        // when
        ThrowingCallable action = () -> loginAuthenticationProvider.authenticate(testAuthentication);

        // then
        assertThatThrownBy(action).isInstanceOf(DisabledException.class);
    }
}