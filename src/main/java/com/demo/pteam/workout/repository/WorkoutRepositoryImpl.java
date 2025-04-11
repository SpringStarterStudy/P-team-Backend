package com.demo.pteam.workout.repository;

import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkoutRepositoryImpl implements WorkoutRepository {

    private final WorkoutJpaRepository workoutRepository;

    @Override
    public List<WorkoutEntity> findByTrainerId(Long trainerId) {
        return workoutRepository.findByTrainerId(trainerId);
    }
}
