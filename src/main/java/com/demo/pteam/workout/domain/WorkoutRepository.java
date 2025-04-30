package com.demo.pteam.workout.domain;

import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository {

    List<WorkoutEntity> findByTrainerId(Long trainerId);

    Optional<WorkoutEntity> findById(Long requestId);

}

