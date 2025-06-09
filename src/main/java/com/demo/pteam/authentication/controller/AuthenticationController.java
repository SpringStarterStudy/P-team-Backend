package com.demo.pteam.authentication.controller;

import com.demo.pteam.authentication.controller.dto.SignupRequest;
import com.demo.pteam.authentication.controller.dto.SignupResponse;
import com.demo.pteam.authentication.service.SignupService;
import com.demo.pteam.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auths")
@RequiredArgsConstructor
public class AuthenticationController {
    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody @Valid SignupRequest signupRequest) {
        SignupResponse response = signupService.signup(signupRequest);
        return ResponseEntity.created(URI.create("/api/members/" + response.id()))
                .body(ApiResponse.success(HttpStatus.CREATED, "회원가입 성공", response));
    }
}
