package com.demo.pteam.schedule.repository.dto;

import com.demo.pteam.schedule.domain.Schedule;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScheduleDto(
        Long id,
        Long accountId,
        Long userId,
        Long trainerId,
        String nickname,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public Schedule toSchedule() {
        return Schedule.builder()
                .id(id)
                .userId(userId)
                .trainerId(trainerId)
                .nickname(nickname)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
