package com.demo.pteam.security.authentication.dto;

import com.demo.pteam.security.principal.UserPrincipal;

public record JwtReissueResult(
        UserPrincipal principal,
        JwtToken token
) {
}
