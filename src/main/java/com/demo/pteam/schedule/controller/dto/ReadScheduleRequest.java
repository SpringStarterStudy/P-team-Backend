package com.demo.pteam.schedule.controller.dto;

import com.demo.pteam.schedule.domain.ScheduleDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

public record ReadScheduleRequest(
        @NotBlank(message = "roleType을 입력해주세요.")
        @Pattern(regexp = "^(user|trainer)$", message = "user 또는 trainer만 입력 가능합니다.")
        String roleType,
        @Positive(message = "년도 입력 형식이 올바르지 않습니다.")
        Integer year,
        @Range(min = 1, max = 12, message = "1 이상 12 이하인 값으로 입력해주세요.")
        Integer month
) {
    public ScheduleDate toScheduleDate() {
        return ScheduleDate.of(year, month);
    }
}
