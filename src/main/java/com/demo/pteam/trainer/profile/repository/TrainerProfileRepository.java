package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.trainer.profile.domain.TrainerProfile;

import java.util.Optional;

public interface TrainerProfileRepository {
    void save(TrainerProfile trainerProfile);
    Optional<TrainerProfile> findByUserId(Long userId);
    void delete(Long profileId);
}
