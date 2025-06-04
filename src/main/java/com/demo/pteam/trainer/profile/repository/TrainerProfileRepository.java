package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;

import java.util.Optional;

public interface TrainerProfileRepository {
    void save(TrainerProfile trainerProfile);
    Optional<TrainerProfileEntity> findEntityByUserId(Long userId);
}
