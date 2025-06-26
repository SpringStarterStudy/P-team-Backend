package com.demo.pteam.schedule.controller.dto;

import com.demo.pteam.schedule.domain.ScheduleDate;

public record ReadScheduleRequest(
        String roleType,
        Integer year,
        Integer month
) {
    public ScheduleDate toScheduleDate() {
        return ScheduleDate.of(year, month);
    }
}
