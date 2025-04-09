package com.demo.pteam.security.login.dto;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;

public record LocalAccountDto(
        Long id,
        String username,
        String password,
        String email,
        String name,
        String nickname,
        AccountStatus status,
        Role role
) {
}
