package com.demo.pteam.security.login;

import com.demo.pteam.security.authentication.JwtUserDetails;
import com.demo.pteam.security.dto.LoginAccountInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class LoginUserDetails implements UserDetails, JwtUserDetails<LoginAccountInfo> {
    private final LoginAccountInfo account;
    private final List<GrantedAuthority> authorities;

    public LoginUserDetails(LoginAccountInfo account, List<GrantedAuthority> authorities) {
        this.account = account;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
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
