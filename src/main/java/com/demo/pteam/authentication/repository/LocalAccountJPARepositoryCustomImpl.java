package com.demo.pteam.authentication.repository;

import com.demo.pteam.authentication.repository.dto.LocalAccountDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.demo.pteam.authentication.repository.entity.QLocalAccountEntity.localAccountEntity;

@Repository
@RequiredArgsConstructor
public class LocalAccountJPARepositoryCustomImpl implements LocalAccountJPARepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<LocalAccountDto> findByActiveUsername(String username) {
        Optional<Tuple> result = Optional.ofNullable(
                queryFactory
                        .select(
                                localAccountEntity.id,
                                localAccountEntity.activeUsername,
                                localAccountEntity.password,
                                localAccountEntity.status,
                                localAccountEntity.role
                        )
                        .from(localAccountEntity)
                        .where(localAccountEntity.activeUsername.eq(username))
                        .fetchOne()
        );
        return createLocalAccountDto(result);
    }

    public Optional<LocalAccountDto>createLocalAccountDto(Optional<Tuple> result) {
        return result.map(tuple ->
                LocalAccountDto.builder()
                        .id(tuple.get(localAccountEntity.id))
                        .username(tuple.get(localAccountEntity.activeUsername))
                        .password(tuple.get(localAccountEntity.password))
                        .status(tuple.get(localAccountEntity.status))
                        .role(tuple.get(localAccountEntity.role))
                        .build()
        );
    }
}
