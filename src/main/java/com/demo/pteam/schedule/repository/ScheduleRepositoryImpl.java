package com.demo.pteam.schedule.repository;

import com.demo.pteam.schedule.repository.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJPARepository scheduleJPARepository;

    @Override
    public List<ScheduleDto> findByUserIdWithinPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        return scheduleJPARepository.findByUserIdWithinPeriod(userId, start, end);
    }

    @Override
    public List<ScheduleDto> findByTrainerIdWithinPeriod(Long trainerId, LocalDateTime start, LocalDateTime end) {
        return scheduleJPARepository.findByTrainerIdWithinPeriod(trainerId, start, end);
    }
}
