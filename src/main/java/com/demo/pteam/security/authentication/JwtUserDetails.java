package com.demo.pteam.security.authentication;

import com.demo.pteam.security.dto.AccountInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtUserDetails<T extends AccountInfo> {
    Collection<? extends GrantedAuthority> getAuthorities();
    T getAccount();
    boolean isEnabled();
    boolean isDeleted();
    boolean isUnverified();
    boolean isSuspended();
}
