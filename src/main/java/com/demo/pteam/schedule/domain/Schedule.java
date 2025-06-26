package com.demo.pteam.schedule.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Schedule {
    private final Long id;
    private final Long userId;
    private final Long trainerId;
    private final String nickname;
    private final LocalDate date;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    @Builder
    private Schedule(Long id, Long userId, Long trainerId, String nickname, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.userId = userId;
        this.trainerId = trainerId;
        this.nickname = nickname;
        this.date = startTime.toLocalDate();
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
