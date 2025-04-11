package com.demo.pteam.security.principal;

import com.demo.pteam.authentication.domain.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record UserPrincipal(
        @JsonIgnore Long id,
        Role role,
        boolean verified
) {
}
