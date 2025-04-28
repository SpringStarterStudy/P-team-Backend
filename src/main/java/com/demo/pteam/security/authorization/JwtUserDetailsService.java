package com.demo.pteam.security.authorization;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.authorization.dto.JwtAccountInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class JwtUserDetailsService {
    public final AccountService accountService;

    public JwtUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    public JwtUserDetails<JwtAccountInfo> loadUserById(Long accountId) throws UsernameNotFoundException {
        try {
            JwtAccountInfo jwtAccount = accountService.getJwtAccount(accountId);
            return new JwtUserDetailsImpl(jwtAccount, List.of(createGrantedAuthority(jwtAccount.role())));
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    private GrantedAuthority createGrantedAuthority(Role role) {
        return new SimpleGrantedAuthority(role.name());
    }
}
