package com.demo.pteam.security.principal;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.authentication.JwtUserDetails;
import com.demo.pteam.security.dto.AccountInfo;
import io.jsonwebtoken.Claims;

public class PrincipalFactory {
    public static <T extends AccountInfo>UserPrincipal fromUser(JwtUserDetails<T> userDetails) {
        T account = userDetails.getAccount();
        boolean verified = !userDetails.isUnverified();
        return new UserPrincipal(account.id(), account.role(), verified);
    }

    public static UserPrincipal fromClaims(Claims claims) throws IllegalArgumentException {
        Long id = Long.valueOf(claims.getSubject());
        Role role = Role.valueOf(claims.get("role", String.class));
        Boolean verified = claims.get("verified", Boolean.class);
        return new UserPrincipal(id, role, verified);
    }
}
