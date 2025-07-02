package com.demo.pteam.schedule.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Schedule {
    private final Long id;
    private final Long userId;
    private final Long trainerId;
    private final String nickname;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
}
