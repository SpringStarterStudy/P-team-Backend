package com.demo.pteam.security.principal;

public record UserPrincipal(
        Long id,
        String username,
        String email,
        String name,
        String nickname
) {
}
