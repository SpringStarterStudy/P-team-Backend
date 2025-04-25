package com.demo.pteam.workout.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.workout.controller.dto.ResponseWorkout;
import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.domain.WorkoutStatus;
import com.demo.pteam.workout.exception.WorkoutErrorCode;
import com.demo.pteam.workout.exception.WorkoutException;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @DisplayName("트레이너 ID로 운동 신청 목록 조회 성공")
    @Test
    void getWorkoutRequests() {
        // given
        AccountEntity mockTrainer = Mockito.mock(AccountEntity.class);
        Mockito.when(mockTrainer.getId()).thenReturn(4L);

        AccountEntity mockUser = Mockito.mock(AccountEntity.class);

        WorkoutEntity workout = WorkoutEntity.builder()
            .trainer(mockTrainer)
            .user(mockUser)
            .status(WorkoutStatus.PENDING)
            .trainingDate(LocalDate.now())
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(1))
            .build();

        List<WorkoutEntity> fakeList = List.of(workout);
        Mockito.when(workoutRepository.findByTrainerId(4L)).thenReturn(fakeList);

        // when
        List<ResponseWorkout> result = workoutService.getWorkoutRequests(4L);

        // then
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals(4L, mockTrainer.getId());

    }

    @DisplayName("운동 신청 상세 조회 성공 - DTO 반환")
    @Test
    void getWorkoutRequestDetail_success() {

        // given
        Long requestId = 1L;
        AccountEntity mockTrainer = Mockito.mock(AccountEntity.class);
        Mockito.when(mockTrainer.getName()).thenReturn("트레이너1");

        AccountEntity mockUser = Mockito.mock(AccountEntity.class);
        Mockito.when(mockUser.getName()).thenReturn("회원1");


        WorkoutEntity fakeWorkout = WorkoutEntity.builder()
            .id(requestId)
            .trainer(mockTrainer)
            .user(mockUser)
            .status(WorkoutStatus.PENDING)
            .trainingDate(LocalDate.now())
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(1))
            .build();

        Mockito.when(workoutRepository.findById(requestId)).thenReturn(
            Optional.ofNullable(fakeWorkout));
        ResponseWorkout expected = ResponseWorkout.builder()
            .id(1L)
            .trainerName("트레이너1")
            .userName("회원1")
            .status("PENDING")
            .trainingDate(fakeWorkout.getTrainingDate())
            .startTime(fakeWorkout.getStartTime())
            .endTime(fakeWorkout.getEndTime())
            .build();

        // when
        ResponseWorkout result = workoutService.getWorkoutRequestDetail(requestId);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        verify(workoutRepository, times(1)).findById(requestId);
    }

    @DisplayName("운동 신청 상세 조회 실패 - 존재하지 않는 ID ApiException")
    @Test
    void getWorkoutRequestDetail_fail() {

        // given
        Long requestId = 999L;
        Mockito.when(workoutRepository.findById(requestId)).thenThrow(new WorkoutException(
            WorkoutErrorCode.WORKOUT_REQUEST_NOT_FOUND));

        // when
        ApiException ex = assertThrows(ApiException.class, () -> {
            workoutService.getWorkoutRequestDetail(requestId);
        });

        // then
        assertThat(ex.getErrorCode()).isEqualTo(WorkoutErrorCode.WORKOUT_REQUEST_NOT_FOUND);

        verify(workoutRepository, times(1)).findById(requestId);

        verifyNoMoreInteractions(workoutRepository);

    }
}