package com.demo.pteam.security.dto;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;

public record LoginAccountInfo(
        Long id,
        String username,
        String password,
        AccountStatus status,
        Role role
)  implements AccountInfo {
}
