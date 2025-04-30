package com.demo.pteam.workout.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.workout.controller.dto.RequestWorkout;
import com.demo.pteam.workout.controller.dto.ResponseWorkout;
import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.domain.WorkoutStatus;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private WorkoutRepository workoutRepository;

    @DisplayName("상태 변경 성공 - PENDING → APPROVED")
    @Test
    void changeStatusSuccess_FromPendingToApproved() {

        // given
        AccountEntity mockTrainer = Mockito.mock(AccountEntity.class);
        AccountEntity mockUser = Mockito.mock(AccountEntity.class);

        WorkoutEntity workoutEntity = WorkoutEntity.builder()
            .id(1L)
            .trainer(mockTrainer)
            .user(mockUser)
            .status(WorkoutStatus.PENDING)
            .build();

        RequestWorkout requestWorkout = mock(RequestWorkout.class);
        when(requestWorkout.getStatus()).thenReturn(WorkoutStatus.APPROVED);

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));

        // when
        ResponseWorkout response = workoutService.changeStatus(1L, requestWorkout);

        // then
        assertEquals(WorkoutStatus.APPROVED, response.getStatus());
        assertEquals(WorkoutStatus.APPROVED, workoutEntity.getStatus());
    }

    @DisplayName("상태 변경 성공 - PENDING → REJECTED")
    @Test
    void changeStatusSuccess_FromPendingToRejected() {

        // given
        AccountEntity mockTrainer = Mockito.mock(AccountEntity.class);
        AccountEntity mockUser = Mockito.mock(AccountEntity.class);

        WorkoutEntity workoutEntity = WorkoutEntity.builder()
            .id(1L)
            .trainer(mockTrainer)
            .user(mockUser)
            .status(WorkoutStatus.PENDING)
            .build();

        RequestWorkout requestWorkout = mock(RequestWorkout.class);
        when(requestWorkout.getStatus()).thenReturn(WorkoutStatus.REJECTED);

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));

        // when
        ResponseWorkout response = workoutService.changeStatus(1L, requestWorkout);

        // then
        assertEquals(WorkoutStatus.REJECTED, response.getStatus());
        assertEquals(WorkoutStatus.REJECTED, workoutEntity.getStatus());
    }

    @DisplayName("상태 변경 실패 - 이미 완료된 상태(승인)에서 변경 시 예외 발생")
    @Test
    void changeStatusFail_whenAlreadyApproved() {
        // given
        WorkoutEntity workoutEntity = WorkoutEntity.builder()
            .id(1L)
            .status(WorkoutStatus.APPROVED)
            .trainer(mock(AccountEntity.class))
            .user(mock(AccountEntity.class))
            .trainingDate(LocalDate.now())
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(1))
            .build();

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));

        RequestWorkout requestWorkout = mock(RequestWorkout.class);
        when(requestWorkout.getStatus()).thenReturn(WorkoutStatus.REJECTED);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            workoutService.changeStatus(1L, requestWorkout);
        });

        assertEquals(ErrorCode.WORKOUT_REQUEST_ALREADY_PROCESSED, exception.getErrorCode());
    }

    @DisplayName("상태 변경 실패 - 이미 완료된 상태(거절)에서 변경 시 예외 발생")
    @Test
    void changeStatusFail_whenAlreadyRejected() {
        // given
        WorkoutEntity workoutEntity = WorkoutEntity.builder()
            .id(1L)
            .status(WorkoutStatus.REJECTED)
            .trainer(mock(AccountEntity.class))
            .user(mock(AccountEntity.class))
            .trainingDate(LocalDate.now())
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(1))
            .build();

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));

        RequestWorkout requestWorkout = mock(RequestWorkout.class);
        when(requestWorkout.getStatus()).thenReturn(WorkoutStatus.APPROVED);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            workoutService.changeStatus(1L, requestWorkout);
        });

        assertEquals(ErrorCode.WORKOUT_REQUEST_ALREADY_PROCESSED, exception.getErrorCode());
    }

}