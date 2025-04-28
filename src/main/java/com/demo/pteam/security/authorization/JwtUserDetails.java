package com.demo.pteam.security.authorization;

import com.demo.pteam.security.authorization.dto.AccountInfo;
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
