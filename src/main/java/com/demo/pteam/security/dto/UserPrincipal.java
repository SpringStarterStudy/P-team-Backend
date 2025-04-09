package com.demo.pteam.security.dto;

public record UserPrincipal(
        Long id,
        String username,
        String email,
        String name,
        String nickname
) {
}
