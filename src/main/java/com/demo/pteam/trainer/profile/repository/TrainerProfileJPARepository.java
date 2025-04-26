package com.demo.pteam.trainer.profile.repository;

import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerProfileJPARepository extends JpaRepository<TrainerProfileEntity, Long> {
}
