package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;

import java.util.Optional;

public interface LocalAccountRepository {
    Optional<LocalAccountEntity> findByUsername(String username);
}
