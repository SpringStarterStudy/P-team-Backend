package com.demo.pteam.workout.controller.dto;

import com.demo.pteam.workout.domain.WorkoutStatus;
import lombok.Getter;

@Getter
public class RequestWorkout {
    private WorkoutStatus status;
}
