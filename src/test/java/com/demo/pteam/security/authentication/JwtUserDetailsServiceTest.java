package com.demo.pteam.security.authentication;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.dto.JwtAccountInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {
    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private AccountService accountService;

    @DisplayName("계정 조회")
    @Test
    void loadUserById() {
        // given
        long testAccountId = 1L;

        JwtAccountInfo expectedJwtAccountInfo = new JwtAccountInfo(
                1L,
                Role.ROLE_USER,
                AccountStatus.ACTIVE
        );
        when(accountService.getJwtAccount(testAccountId)).thenReturn(expectedJwtAccountInfo);

        // when
        JwtUserDetails<JwtAccountInfo> userDetails = jwtUserDetailsService.loadUserById(testAccountId);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(JwtUserDetailsImpl.class);
        assertThat(userDetails.getAccount()).isEqualTo(expectedJwtAccountInfo);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo(expectedJwtAccountInfo.role().name());
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isDeleted()).isFalse();
        assertThat(userDetails.isUnverified()).isFalse();
        assertThat(userDetails.isSuspended()).isFalse();
    }

    @DisplayName("계정 조회 - 조회된 계정이 없는 경우")
    @Test
    void loadUserByUsername_unregisteredUsername() {
        // given
        long unregisteredAccountId = 100L;
        when(accountService.getJwtAccount(unregisteredAccountId))
                .thenThrow(new UserNotFoundException("AccountId not found: " + unregisteredAccountId));

        // when
        ThrowingCallable action = () -> jwtUserDetailsService.loadUserById(unregisteredAccountId);

        // then
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(action)
                .withMessage("AccountId not found: " + unregisteredAccountId);
    }
}