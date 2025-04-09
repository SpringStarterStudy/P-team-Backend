package com.demo.pteam.authentication.service;

import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.repository.LocalAccountRepository;
import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import com.demo.pteam.security.dto.LocalAccountDto;
import com.demo.pteam.security.mapper.LocalAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final LocalAccountRepository localAccountRepository;
    private final LocalAccountMapper localAccountMapper;

    public LocalAccountDto getLocalAccount(String username) {
        LocalAccountEntity localAccount = localAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return localAccountMapper.toLocalAccountDto(localAccount);
    }
}
