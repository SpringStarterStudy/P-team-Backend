package com.demo.pteam.authentication.controller;

import com.demo.pteam.authentication.service.LogoutService;
import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final LogoutService logoutService;

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserPrincipal principal, @RequestHeader("Refresh-Token") String refreshToken) {
        logoutService.logout(principal.id(), refreshToken);
        return ResponseEntity.ok(ApiResponse.success("로그아웃 되었습니다."));
    }
}
