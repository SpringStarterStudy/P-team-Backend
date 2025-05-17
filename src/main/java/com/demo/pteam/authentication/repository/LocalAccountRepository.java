package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.LocalAccountDto;

import java.util.Optional;

public interface LocalAccountRepository {
    Optional<LocalAccountDto> findByUsername(String username);
}
