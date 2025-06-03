package com.demo.pteam.authentication.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Signup {
    private final Long id;
    private final String username;
    private final String encodedPassword;
    private final String role;
    private final String email;
    private final String name;
    private final String nickname;
    private final LocalDateTime createdAt = LocalDateTime.now();
}
