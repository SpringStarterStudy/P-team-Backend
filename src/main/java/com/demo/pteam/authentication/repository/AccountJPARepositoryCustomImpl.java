package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.AccountDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.demo.pteam.authentication.repository.entity.QAccountEntity.accountEntity;
import static com.demo.pteam.authentication.repository.entity.QLocalAccountEntity.localAccountEntity;
import static com.demo.pteam.authentication.repository.entity.QSocialAccountEntity.socialAccountEntity;

@Repository
@RequiredArgsConstructor
public class AccountJPARepositoryCustomImpl implements AccountJPARepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AccountDto> findByAccountId(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(AccountDto.class,
                                accountEntity.id,
                                accountEntity.role,
                                localAccountEntity.status,
                                socialAccountEntity.status))
                        .from(accountEntity)
                        .join(localAccountEntity).on(accountEntity.id.eq(localAccountEntity.id))
                        .join(socialAccountEntity).on(accountEntity.id.eq(socialAccountEntity.id))
                        .fetchOne()
        );
    }
}
