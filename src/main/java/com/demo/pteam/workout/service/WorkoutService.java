package com.demo.pteam.workout.service;

import com.demo.pteam.workout.controller.dto.RequestWorkout;
import com.demo.pteam.workout.controller.dto.ResponseWorkout;
import com.demo.pteam.workout.domain.Workout;
import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.exception.WorkoutErrorCode;
import com.demo.pteam.workout.exception.WorkoutException;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    // private final ScheduleService scheduleService;

    @Transactional
    public ResponseWorkout changeStatus(Long requestId, RequestWorkout requestWorkout) {
        WorkoutEntity workoutEntity = workoutRepository.findById(requestId)
            .orElseThrow(() -> new WorkoutException(
                WorkoutErrorCode.WORKOUT_REQUEST_NOT_FOUND));

        Workout workout = new Workout(workoutEntity);
        workout.changeStatus(requestWorkout.getStatus());

        //Todo 일정 생성
//        if (workout.isApproved()) {
//            scheduleService.createSchedule(workout);
//        }

        workoutEntity.updateStatus(workout.getStatus());

        return ResponseWorkout.from(workoutEntity);
    }

    public List<ResponseWorkout> getWorkoutRequests(Long userId) {

        //Todo 권한 확인 후 메서드 분리
        List<WorkoutEntity> workoutEntities = workoutRepository.findByTrainerId(userId);

        return workoutEntities.stream()
            .map(ResponseWorkout::from)
            .toList();
    }

    public ResponseWorkout getWorkoutRequestDetail(Long requestId) {
        WorkoutEntity workoutEntity = workoutRepository.findById(requestId)
            .orElseThrow(() -> new WorkoutException(
                WorkoutErrorCode.WORKOUT_REQUEST_NOT_FOUND));
        return ResponseWorkout.from(workoutEntity);
    }
}
