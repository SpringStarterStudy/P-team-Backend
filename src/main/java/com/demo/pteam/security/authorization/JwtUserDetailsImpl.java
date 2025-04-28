package com.demo.pteam.security.authorization;

import com.demo.pteam.security.authorization.dto.JwtAccountInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtUserDetailsImpl implements JwtUserDetails<JwtAccountInfo> {
    private final JwtAccountInfo account;
    private final List<GrantedAuthority> authorities;

    public JwtUserDetailsImpl(JwtAccountInfo account, List<GrantedAuthority> authorities) {
        this.account = account;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public JwtAccountInfo getAccount() {
        return account;
    }

    @Override
    public boolean isEnabled() {
        return !isSuspended();
    }

    @Override
    public boolean isDeleted() {
        return account.status().isDeleted();
    }

    @Override
    public boolean isUnverified() {
        return account.status().isUnverified();
    }

    @Override
    public boolean isSuspended() {
        return account.status().isSuspended();
    }
}
