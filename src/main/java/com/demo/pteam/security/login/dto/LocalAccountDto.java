package com.demo.pteam.security.login.dto;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;

public record LocalAccountDto(
        Long id,
        String username,
        String password,
        AccountStatus status,
        Role role,
        boolean verified
) {
}
