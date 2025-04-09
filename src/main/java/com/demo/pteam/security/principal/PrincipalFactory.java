package com.demo.pteam.security.principal;

import com.demo.pteam.security.login.dto.LocalAccountDto;

public class PrincipalFactory {
    public static UserPrincipal fromUser(CustomUserDetails userDetails) {
        LocalAccountDto account = userDetails.getAccount();
        return new UserPrincipal(
                account.id(),
                account.username(),
                account.email(),
                account.name(),
                account.nickname()
        );
    }
}
