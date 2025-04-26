package com.demo.pteam.trainer.profile.controller;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
import com.demo.pteam.trainer.profile.service.TrainerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainers/me/profile")
public class TrainerProfileController {

    private final TrainerProfileService trainerProfileService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createProfile(
            @RequestBody TrainerProfileRequest request
    ) {
        Long userId = 4L; // TODO: 임시

        trainerProfileService.createProfile(request, userId);
        return ResponseEntity.status(201).body(ApiResponse.created("트레이너 프로필이 성공적으로 등록되었습니다."));
    }
}