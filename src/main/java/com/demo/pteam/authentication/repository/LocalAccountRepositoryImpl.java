package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.LocalAccountDto;
import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocalAccountRepositoryImpl implements LocalAccountRepository {
    private final LocalAccountJPARepository localAccountJPARepository;

    @Override
    public Optional<LocalAccountDto> findByUsername(String username) {
        return localAccountJPARepository.findByActiveUsername(username);
    }

    @Override
    public LocalAccountEntity save(LocalAccountEntity localAccountEntity) {
        return localAccountJPARepository.save(localAccountEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return localAccountJPARepository.existsByActiveUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return localAccountJPARepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return localAccountJPARepository.existsByNickname(nickname);
    }
}
