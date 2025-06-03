package com.demo.pteam.authentication.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SignupResponse (
        Long id,
        String username,
        String role,
        String email,
        String name,
        String nickname,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
){
}
