package com.demo.pteam.workout.controller;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.workout.controller.dto.ResponseWorkout;
import com.demo.pteam.workout.service.WorkoutService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout-requests")
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ResponseWorkout>>> getWorkoutRequests() {
        return ResponseEntity.ok(ApiResponse.success(workoutService.getWorkoutRequests(4L)));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<ResponseWorkout>> getWorkoutRequests(
        @PathVariable Long requestId) {
        return ResponseEntity.ok(
            ApiResponse.success(workoutService.getWorkoutRequestDetail(requestId)));
    }
}
