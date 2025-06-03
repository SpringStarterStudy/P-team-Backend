package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.LocalAccountDto;
import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;

import java.util.Optional;

public interface LocalAccountRepository {
    Optional<LocalAccountDto> findByUsername(String username);
    LocalAccountEntity save(LocalAccountEntity localAccountEntity);
}
