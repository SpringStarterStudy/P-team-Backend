package com.demo.pteam.workout.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.workout.controller.dto.ResponseWorkout;
import com.demo.pteam.workout.domain.WorkoutRepository;
import com.demo.pteam.workout.repository.entity.StatusType;
import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    void test() {
        // given
        AccountEntity mockTrainer = Mockito.mock(AccountEntity.class);
        Mockito.when(mockTrainer.getId()).thenReturn(4L);

        AccountEntity mockUser = Mockito.mock(AccountEntity.class);

        WorkoutEntity workout = WorkoutEntity.builder()
            .trainer(mockTrainer)
            .user(mockUser)
            .status(StatusType.PENDING)
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
}