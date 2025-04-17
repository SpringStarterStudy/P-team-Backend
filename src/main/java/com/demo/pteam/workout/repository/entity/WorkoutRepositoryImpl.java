package com.demo.pteam.workout.repository.entity;

import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.repository.WorkoutJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkoutRepositoryImpl implements WorkoutRepository {

    private final WorkoutJpaRepository workoutRepository;

    @Override
    public Optional<WorkoutEntity> findById(Long requestId) {
        return workoutRepository.findById(requestId);
    }

}
