package com.demo.pteam.authentication.controller;

import com.demo.pteam.authentication.service.LogoutService;
import com.demo.pteam.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auths")
@RequiredArgsConstructor
public class AuthenticationController {
    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        logoutService.logout(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("로그아웃 되었습니다."));
    }
}
