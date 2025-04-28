package com.demo.pteam.authentication.repository.dto;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;

public record AccountDto(
        Long id,
        Role role,
        AccountStatus localStatus,
        AccountStatus socialStatus
) {
    public AccountStatus status() {
        return localStatus != null ? localStatus : socialStatus;
    }
}
