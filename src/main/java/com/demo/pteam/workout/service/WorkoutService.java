package com.demo.pteam.workout.service;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.workout.controller.dto.RequestWorkout;
import com.demo.pteam.workout.controller.dto.ResponseWorkout;
import com.demo.pteam.workout.domain.Workout;
import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
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
            .orElseThrow(() -> new ApiException(
                ErrorCode.WORKOUT_REQUEST_NOT_FOUND));

        Workout workout = new Workout(workoutEntity);
        workout.changeStatus(requestWorkout.getStatus());

        //Todo 일정 생성
//        if (workout.isApproved()) {
//            scheduleService.createSchedule(workout);
//        }

        workoutEntity.updateStatus(workout.getStatus());

        return ResponseWorkout.from(workoutEntity);
    }
}
