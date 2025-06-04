package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;

import java.util.Optional;

public interface TrainerProfileJPARepositoryCustom {
  Optional<TrainerProfileEntity> findDetailedProfileEntityByUserId(Long userId);
}
