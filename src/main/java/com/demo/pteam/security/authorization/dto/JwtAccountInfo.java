package com.demo.pteam.security.authorization.dto;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;

public record JwtAccountInfo(
        Long id,
        Role role,
        AccountStatus status
) implements AccountInfo {
}
