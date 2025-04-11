package com.demo.pteam.security.principal;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.exception.InvalidJwtException;
import com.demo.pteam.security.login.dto.LocalAccountDto;
import io.jsonwebtoken.Claims;

public class PrincipalFactory {
    public static UserPrincipal fromUser(CustomUserDetails userDetails) {
        LocalAccountDto account = userDetails.getAccount();
        boolean verified = !userDetails.isUnverified();
        return new UserPrincipal(account.id(), account.role(), verified);
    }

    public static UserPrincipal fromClaims(Claims claims) {
        try {
            Long id = Long.valueOf(claims.getSubject());
            Role role = claims.get("role", Role.class);
            Boolean verified = claims.get("verified", Boolean.class);
            return new UserPrincipal(id, role, verified);
        } catch (NullPointerException | NumberFormatException | ClassCastException e) {
            throw new InvalidJwtException("JWT Claims parsing failed.", e);
        }
    }
}
