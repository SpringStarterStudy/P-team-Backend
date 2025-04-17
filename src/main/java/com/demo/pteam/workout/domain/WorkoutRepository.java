package com.demo.pteam.workout.domain;

import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.util.Optional;

public interface WorkoutRepository {

    Optional<WorkoutEntity> findById(Long requestId);

}