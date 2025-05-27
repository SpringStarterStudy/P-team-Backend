package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.authentication.repository.entity.QAccountEntity;
import com.demo.pteam.trainer.address.repository.entity.QTrainerAddressEntity;
import com.demo.pteam.trainer.profile.repository.entity.QTrainerProfileEntity;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainerProfileJPARepositoryCustomImpl implements TrainerProfileJPARepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<TrainerProfileEntity> findDetailedProfileEntityByUserId(Long userId) {
    QTrainerProfileEntity profile = QTrainerProfileEntity.trainerProfileEntity;
    QTrainerAddressEntity address = QTrainerAddressEntity.trainerAddressEntity;
    QAccountEntity account = QAccountEntity.accountEntity;

    return Optional.ofNullable(
            queryFactory
                    .select(profile)
                    .from(profile)
                    .leftJoin(profile.address, address).fetchJoin()
                    .leftJoin(profile.trainer, account).fetchJoin()
                    .where(profile.trainer.id.eq(userId))
                    .fetchOne()
    );
  }
}
