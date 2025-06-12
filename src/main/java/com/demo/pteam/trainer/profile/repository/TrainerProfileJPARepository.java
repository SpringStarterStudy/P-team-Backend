package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerProfileJPARepository extends JpaRepository<TrainerProfileEntity, Long> {
  Optional<TrainerProfileEntity> findByTrainerId(Long userId);
}
