package com.demo.pteam.workout.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WorkoutStatusTest {


    @DisplayName("PENDING 상태는 완료되지 않은 상태")
    @Test
    void testIsFinishedForPending() {
        // given
        WorkoutStatus status = WorkoutStatus.PENDING;

        // when
        boolean result = status.isFinished();

        // then
        assertFalse(result, "PENDING 상태는 완료되지 않은 상태여야 한다.");
    }


    @DisplayName("APPROVED 상태는 완료된 상태")
    @Test
    void testIsFinishedForApproved() {
        // given
        WorkoutStatus status = WorkoutStatus.APPROVED;

        // when
        boolean result = status.isFinished();

        // then
        assertTrue(result, "APPROVED 상태는 완료된 상태여야 한다.");
    }

    @DisplayName("REJECTED 상태는 완료된 상태")
    @Test
    void testIsFinishedForRejected() {
        // given
        WorkoutStatus status = WorkoutStatus.REJECTED;

        // when
        boolean result = status.isFinished();

        // then
        assertTrue(result, "REJECTED 상태는 완료된 상태여야 한다.");
    }
}