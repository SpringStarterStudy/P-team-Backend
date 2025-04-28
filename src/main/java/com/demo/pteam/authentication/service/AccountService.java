package com.demo.pteam.authentication.service;

import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.repository.AccountRepository;
import com.demo.pteam.authentication.repository.LocalAccountRepository;
import com.demo.pteam.authentication.repository.dto.AccountDto;
import com.demo.pteam.authentication.repository.dto.LocalAccountDto;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.authentication.mapper.AccountMapper;
import com.demo.pteam.security.dto.LoginAccountInfo;
import com.demo.pteam.security.login.mapper.LocalAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final LocalAccountRepository localAccountRepository;
    private final AccountMapper accountMapper;
    private final LocalAccountMapper localAccountMapper;

    public LoginAccountInfo getLoginAccount(String username) {
        LocalAccountDto localAccount = localAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return localAccountMapper.toLoginAccountInfo(localAccount);
    }

    public JwtAccountInfo getJwtAccount(Long accountId) {
        AccountDto account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("AccountId not found: " + accountId));
        return accountMapper.toJwtAccountInfo(account);
    }
}
