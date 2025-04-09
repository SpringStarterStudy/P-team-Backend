package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocalAccountRepositoryImpl implements LocalAccountRepository {
    private final LocalAccountJPARepository localAccountJPARepository;

    @Override
    public Optional<LocalAccountEntity> findByUsername(String username) {
        return localAccountJPARepository.findByActiveUsername(username);
    }
}
