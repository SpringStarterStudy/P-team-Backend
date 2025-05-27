package com.demo.pteam.trainer.profile.controller;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
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
   * 트레이너 프로필 수정 API
   * @param request 트레이너 프로필 요청 DTO
   * @return 수정 성공 여부
   */
  @PutMapping
  public ResponseEntity<ApiResponse<Void>> updateProfile(
          @RequestBody @Valid TrainerProfileRequest request
  ) {
    Long userId = 3L; // TODO: 로그인 사용자 임시

    trainerProfileService.updateProfile(request, userId);
    return ResponseEntity.ok(ApiResponse.success("트레이너 프로필이 성공적으로 수정되었습니다."));
  }

}