package com.demo.pteam.workout.domain;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Workout {
    private Long id;
    //todo 계정
    private WorkoutStatus status;
    private LocalDate trainingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // 생성자
    public Workout(WorkoutEntity entity) {
        this.id = entity.getId();
        this.status = entity.getStatus();
        this.trainingDate = entity.getTrainingDate();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
    }

    public void changeStatus(WorkoutStatus newStatus) {
        if (this.status.isFinished()) {
            throw new ApiException(ErrorCode.WORKOUT_REQUEST_ALREADY_PROCESSED);
        }
        this.status = newStatus;
    }

    public boolean isApproved() {
        return this.status == WorkoutStatus.APPROVED;
    }


}
