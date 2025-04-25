package com.demo.pteam.workout.controller.dto;

import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseWorkout {

    private Long id;
    private String trainerName;
    private String userName;
    private String status;
    private LocalDate trainingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createAt;

    public static ResponseWorkout from (WorkoutEntity entity) {
        return ResponseWorkout.builder()
            .id(entity.getId())
            .trainerName(entity.getTrainer().getName())
            .userName(entity.getUser().getName())
            .status(entity.getStatus().name())
            .trainingDate(entity.getTrainingDate())
            .startTime(entity.getStartTime())
            .endTime(entity.getEndTime())
            .createAt(entity.getCreatedAt())
            .build();
    }
}
