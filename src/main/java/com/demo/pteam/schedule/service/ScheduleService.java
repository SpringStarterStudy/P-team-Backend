package com.demo.pteam.schedule.service;

import com.demo.pteam.schedule.controller.dto.ReadScheduleRequest;
import com.demo.pteam.schedule.controller.dto.ScheduleResponse;
import com.demo.pteam.schedule.domain.ScheduleDate;
import com.demo.pteam.schedule.repository.ScheduleRepository;
import com.demo.pteam.schedule.repository.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @PreAuthorize("authentication.principal.id == #userId")
    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllSchedules(Long userId, ReadScheduleRequest request) {
        ScheduleDate date = request.toScheduleDate();
        List<ScheduleDto> scheduleDtoList = switch (request.roleType()) {
            case "user" -> findAllUserSchedules(userId, date);
            case "trainer" -> findAllTrainerSchedules(userId, date);
            default -> throw new IllegalStateException("Unexpected value: " + request.roleType());
        };
        return scheduleDtoList.stream()
                .filter(dto -> dto.accountId().equals(userId))
                .map(dto -> ScheduleResponse.from(dto.toSchedule()))
                .toList();
    }

    public List<ScheduleDto> findAllUserSchedules(Long userId, ScheduleDate date) {
        return scheduleRepository.findByUserIdWithinPeriod(userId, date.getStartOfMonth(), date.getEndOfMonth());
    }

    public List<ScheduleDto> findAllTrainerSchedules(Long trainerId, ScheduleDate date) {
        return scheduleRepository.findByTrainerIdWithinPeriod(trainerId, date.getStartOfMonth(), date.getEndOfMonth());
    }
}
