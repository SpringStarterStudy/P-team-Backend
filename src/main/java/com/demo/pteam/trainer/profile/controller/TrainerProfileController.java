package com.demo.pteam.trainer.profile.controller;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileResponse;
import com.demo.pteam.trainer.profile.service.TrainerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainers/me/profile")
public class TrainerProfileController {

  private final TrainerProfileService trainerProfileService;

  /**
   * 트레이너 프로필 등록 API
   * @param request 트레이너 프로필 요청 DTO
   * @return 등록 성공 여부
   */
  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createProfile(
          @RequestBody @Valid TrainerProfileRequest request
  ) {
    Long userId = 4L; // TODO: 로그인 사용자 임시

    trainerProfileService.createProfile(request, userId);
    return ResponseEntity.status(201).body(ApiResponse.created("트레이너 프로필이 성공적으로 등록되었습니다."));
  }

  /**
   * 트레이너 프로필 조회 API (사용자 본인)
   * @return 트레이너 프로필 응답 DTO
   */
  @GetMapping
  public ResponseEntity<ApiResponse<TrainerProfileResponse>> getProfile() {
    Long userId = 4L; // TODO: 로그인 사용자 임시

    TrainerProfileResponse response = trainerProfileService.getProfile(userId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

}