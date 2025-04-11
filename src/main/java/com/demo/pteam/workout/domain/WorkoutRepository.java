package com.demo.pteam.workout.domain;

import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.util.List;

public interface WorkoutRepository {

    List<WorkoutEntity> findByTrainerId(Long trainerId);

    WorkoutEntity findById(Long requestId);
}
