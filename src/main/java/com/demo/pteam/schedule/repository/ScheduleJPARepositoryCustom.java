package com.demo.pteam.schedule.repository;

import com.demo.pteam.schedule.repository.dto.ScheduleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleJPARepositoryCustom {
    List<ScheduleDto> findByUserIdWithinPeriod(Long userId, LocalDateTime start, LocalDateTime end);
    List<ScheduleDto> findByTrainerIdWithinPeriod(Long trainerId, LocalDateTime start, LocalDateTime end);
}
