package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.LocalAccountDto;

import java.util.Optional;

public interface LocalAccountJPARepositoryCustom {
    Optional<LocalAccountDto> findByActiveUsername(String username);
}
