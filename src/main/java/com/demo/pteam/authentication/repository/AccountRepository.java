package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.AccountDto;

import java.util.Optional;

public interface AccountRepository {
    Optional<AccountDto> findById(Long id);
}
