package com.demo.pteam.workout.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WorkoutTest {

    @DisplayName("Workout 상태 변경 테스트 - 정상적으로 상태 변경")
    @Test
    void testChangeStatusSuccess() {
        // given
        WorkoutEntity workoutEntity = WorkoutEntity.builder()
            .id(1L)
            .status(WorkoutStatus.PENDING)
            .build();
        Workout workout = new Workout(workoutEntity);

        // when
        workout.changeStatus(WorkoutStatus.APPROVED);

        // then
        assertEquals(WorkoutStatus.APPROVED, workout.getStatus());
    }

    @DisplayName("Workout 상태 변경 테스트 - 이미 완료된 상태(승인)에서 상태 변경 시 예외 발생")
    @Test
    void testChangeStatusFailAlreadyApproved() {
        // given
        WorkoutEntity workoutEntity = WorkoutEntity.builder()
            .id(1L)
            .status(WorkoutStatus.APPROVED)
            .build();
        Workout workout = new Workout(workoutEntity);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            workout.changeStatus(WorkoutStatus.APPROVED);
        });
        assertEquals(ErrorCode.WORKOUT_REQUEST_ALREADY_PROCESSED, exception.getErrorCode());
    }

    @DisplayName("Workout 상태 변경 테스트 - 이미 완료된 상태(거절)에서 상태 변경 시 예외 발생")
    @Test
    void testChangeStatusFailAlreadyRejected() {
        // given
        WorkoutEntity workoutEntity = WorkoutEntity.builder()
            .id(1L)
            .status(WorkoutStatus.REJECTED)
            .build();
        Workout workout = new Workout(workoutEntity);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            workout.changeStatus(WorkoutStatus.APPROVED);
        });
        assertEquals(ErrorCode.WORKOUT_REQUEST_ALREADY_PROCESSED, exception.getErrorCode());
    }
}