package com.demo.pteam.security.principal;

import com.demo.pteam.security.login.dto.LoginAccountInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final LoginAccountInfo account;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(LoginAccountInfo account, List<GrantedAuthority> authorities) {
        this.account = account;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public LoginAccountInfo getAccount() {
        return account;
    }

    @Override
    public String getPassword() {
        return account.password();
    }

    @Override
    public String getUsername() {
        return account.username();
    }

    @Override
    public boolean isEnabled() {
        return !isSuspended();
    }

    public boolean isDeleted() {
        return account.status().isDeleted();
    }

    public boolean isUnverified() {
        return account.status().isUnverified();
    }

    public boolean isSuspended() {
        return account.status().isSuspended();
    }
}
