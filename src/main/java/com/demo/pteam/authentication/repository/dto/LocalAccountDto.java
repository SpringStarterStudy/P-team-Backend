package com.demo.pteam.authentication.repository.dto;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import lombok.Builder;

@Builder
public record LocalAccountDto(
        Long id,
        String username,
        String password,
        AccountStatus status,
        Role role
) {
}
