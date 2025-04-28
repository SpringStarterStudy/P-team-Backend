package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountJPARepository accountJPARepository;

    @Override
    public Optional<AccountDto> findById(Long id) {
        return accountJPARepository.findByAccountId(id);
    }
}
