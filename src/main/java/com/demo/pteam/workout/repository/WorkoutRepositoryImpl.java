package com.demo.pteam.workout.repository;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.global.exception.ErrorCode;
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

    @Override
    public WorkoutEntity findById(Long requestId) {
        return workoutRepository.findById(requestId)
            .orElseThrow(() -> new ApiException(ErrorCode.WORKOUT_REQUEST_NOT_FOUND));
    }
}
