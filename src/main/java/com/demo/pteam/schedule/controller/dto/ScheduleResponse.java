package com.demo.pteam.schedule.controller.dto;

import com.demo.pteam.schedule.domain.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ScheduleResponse(
        Long scheduleId,
        Long userId,
        Long trainerId,
        String nickname,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm")
        LocalDateTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalDateTime endTime
) {
    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getId())
                .userId(schedule.getUserId())
                .trainerId(schedule.getTrainerId())
                .nickname(schedule.getNickname())
                .date(schedule.getDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
