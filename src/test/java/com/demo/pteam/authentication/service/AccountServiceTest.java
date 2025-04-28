package com.demo.pteam.authentication.service;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.repository.LocalAccountRepository;
import com.demo.pteam.authentication.repository.dto.LocalAccountDto;
import com.demo.pteam.security.dto.LoginAccountInfo;
import com.demo.pteam.security.login.mapper.LocalAccountMapper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private LocalAccountRepository localAccountRepository;

    @Mock
    private LocalAccountMapper localAccountMapper;

    private final static PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @DisplayName("계정 정보 조회")
    @Test
    void getLoginAccount() {
        // given
        String username = "username";
        String password = "password";
        LocalAccountDto testLocalAccountDto = new LocalAccountDto(
                1L,
                username,
                passwordEncoder.encode(password),
                AccountStatus.ACTIVE,
                Role.ROLE_USER
        );
        LoginAccountInfo testLoginAccountInfo = new LoginAccountInfo(
                1L,
                username,
                passwordEncoder.encode(password),
                AccountStatus.ACTIVE,
                Role.ROLE_USER
        );
        when(localAccountRepository.findByUsername(username)).thenReturn(Optional.of(testLocalAccountDto));
        when(localAccountMapper.toLoginAccountInfo(testLocalAccountDto)).thenReturn(testLoginAccountInfo);

        // when
        LoginAccountInfo accountInfo = accountService.getLoginAccount(username);

        // then
        assertThat(accountInfo).isNotNull();
    }

    @DisplayName("계정 정보 조회 - 조회된 결과가 없을 경우")
    @Test
    void getLoginAccount_isNotExist() {
        // given
        String unregisteredUsername = "unregistered";
        when(localAccountRepository.findByUsername(unregisteredUsername)).thenReturn(Optional.empty());

        // when
        ThrowingCallable action = () -> accountService.getLoginAccount(unregisteredUsername);

        // then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(action)
                .withMessage("User not found: " + unregisteredUsername);
    }
}