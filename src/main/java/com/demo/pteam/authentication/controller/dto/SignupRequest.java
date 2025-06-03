package com.demo.pteam.authentication.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignupRequest(
        String username,
        String password,
        @JsonProperty("password_confirm")
        String passwordConfirm,
        String role,
        String email,
        String name,
        String nickname
) {
}
