package com.demo.pteam.workout.controller.dto;


import com.demo.pteam.workout.domain.WorkoutStatus;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseWorkout {

    private final Long id;
    private final String trainerName;
    private final String userName;
    private final WorkoutStatus status;
    private final LocalDate trainingDate;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final LocalDateTime createdAt;


    public static ResponseWorkout from (WorkoutEntity entity) {
        return ResponseWorkout.builder()
            .id(entity.getId())
            .trainerName(entity.getTrainer().getName())
            .userName(entity.getUser().getName())
            .status(entity.getStatus())
            .trainingDate(entity.getTrainingDate())
            .startTime(entity.getStartTime())
            .endTime(entity.getEndTime())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
