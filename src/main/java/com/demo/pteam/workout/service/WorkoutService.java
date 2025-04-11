package com.demo.pteam.workout.service;

import com.demo.pteam.workout.controller.dto.ResponseWorkout;
import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    public List<ResponseWorkout> getWorkoutRequests(Long userId) {

        //Todo 권한 확인 후 메서드 분리
        List<WorkoutEntity> workoutEntities = workoutRepository.findByTrainerId(userId);

        return workoutEntities.stream()
            .map(ResponseWorkout::from)
            .toList();
    }

    public ResponseWorkout getWorkoutRequestDetail(Long requestId) {
            WorkoutEntity workoutEntity = workoutRepository.findById(requestId);
            return ResponseWorkout.from(workoutEntity);
    }
}
