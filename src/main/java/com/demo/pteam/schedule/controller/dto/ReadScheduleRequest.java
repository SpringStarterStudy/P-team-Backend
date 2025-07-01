package com.demo.pteam.schedule.controller.dto;

import com.demo.pteam.schedule.domain.ScheduleDate;
import com.demo.pteam.schedule.validator.YearRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Range;

public record ReadScheduleRequest(
        @NotBlank(message = "roleType을 입력해주세요.")
        @Pattern(regexp = "^(user|trainer)$", message = "user 또는 trainer만 입력 가능합니다.")
        String roleType,
        @YearRange
        Integer year,
        @Range(min = 1, max = 12, message = "1 이상 12 이하인 값으로 입력해주세요.")
        Integer month
) {
    public ScheduleDate toScheduleDate() {
        return ScheduleDate.of(year, month);
    }
}
