package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.mapper.TrainerProfileMapper;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TrainerProfileRepositoryImpl implements TrainerProfileRepository {

  private final TrainerProfileJPARepository trainerProfileJPARepository;
  private final EntityManager em;

  @Override
  public void save(TrainerProfile profile) {

    AccountEntity trainer = em.getReference(AccountEntity.class, profile.getUserId());
    TrainerAddressEntity address = em.getReference(TrainerAddressEntity.class, profile.getAddressId());

    TrainerProfileEntity entity = TrainerProfileMapper.toEntity(profile, trainer, address);
    trainerProfileJPARepository.save(entity);
  }

  @Override
  public Optional<TrainerProfileEntity> findEntityByUserId(Long userId) {
    return trainerProfileJPARepository.findDetailedProfileEntityByUserId(userId);
  }

}