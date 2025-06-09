package com.demo.pteam.authentication.service;

import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.repository.LocalAccountRepository;
import com.demo.pteam.authentication.repository.dto.LocalAccountDto;
import com.demo.pteam.security.login.dto.LoginAccountInfo;
import com.demo.pteam.security.login.mapper.LocalAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final LocalAccountRepository localAccountRepository;
    private final LocalAccountMapper localAccountMapper;

    public LoginAccountInfo getLoginAccount(String username) {
        LocalAccountDto localAccount = localAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return localAccountMapper.toLoginAccountInfo(localAccount);
    }

    public boolean isUniqueByUsername(String username) {
        return !localAccountRepository.existsByUsername(username);
    }

    public boolean isUniqueByEmail(String email) {
        return !localAccountRepository.existsByEmail(email);
    }

    public boolean isUniqueNickname(String nickname) {
        return !localAccountRepository.existsByNickname(nickname);
    }
}
