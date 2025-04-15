package com.demo.pteam.security.login;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.login.dto.LoginAccountInfo;
import com.demo.pteam.security.principal.CustomUserDetails;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private AccountService accountService;

    private final static PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @DisplayName("계정 조회")
    @Test
    void loadUserByUsername_success() {
        // given
        String username = "username";
        String password = "1234567aA!";

        LoginAccountInfo testAccountInfo = new LoginAccountInfo(
                1L,
                username,
                passwordEncoder.encode(password),
                AccountStatus.ACTIVE,
                Role.ROLE_USER
        );
        when(accountService.getLoginAccount(username)).thenReturn(testAccountInfo);

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertThat(customUserDetails.getAccount()).isEqualTo(testAccountInfo);
        assertThat(customUserDetails.getPassword()).isEqualTo(testAccountInfo.password());
        assertThat(customUserDetails.getUsername()).isEqualTo(testAccountInfo.username());
        assertThat(customUserDetails.getAuthorities()).hasSize(1);
        assertThat(customUserDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo(testAccountInfo.role().name());
        assertThat(customUserDetails.isEnabled()).isTrue();
        assertThat(customUserDetails.isDeleted()).isFalse();
        assertThat(customUserDetails.isUnverified()).isFalse();
        assertThat(customUserDetails.isSuspended()).isFalse();
    }

    @DisplayName("계정 조회 - 계정이 없는 경우")
    @Test
    void loadUserByUsername_unRegisteredUsername() {
        // given
        String unregisteredUsername = "unregistered";
        when(accountService.getLoginAccount(unregisteredUsername))
                .thenThrow(new UserNotFoundException("User not found: " + unregisteredUsername));

        // when
        ThrowingCallable action = () -> customUserDetailsService.loadUserByUsername(unregisteredUsername);

        // then
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(action)
                .withMessage("User not found: " + unregisteredUsername);
    }
}