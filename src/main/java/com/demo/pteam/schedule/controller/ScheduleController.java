package com.demo.pteam.schedule.controller;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.schedule.controller.dto.ReadScheduleRequest;
import com.demo.pteam.schedule.controller.dto.ScheduleResponse;
import com.demo.pteam.schedule.service.ScheduleService;
import com.demo.pteam.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> readSchedules(@AuthenticationPrincipal UserPrincipal principal,
                                                           @ModelAttribute ReadScheduleRequest requestParams) {
        List<ScheduleResponse> schedules = scheduleService.findAllSchedules(principal.id(), requestParams);
        return ResponseEntity.ok(ApiResponse.success("회원정보 조회 성공", schedules));
    }
}
