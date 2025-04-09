package com.demo.pteam.security.factory;

import com.demo.pteam.security.dto.CustomUserDetails;
import com.demo.pteam.security.dto.LocalAccountDto;
import com.demo.pteam.security.dto.UserPrincipal;

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
