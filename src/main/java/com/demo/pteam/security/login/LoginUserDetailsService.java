package com.demo.pteam.security.login;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.dto.LoginAccountInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class LoginUserDetailsService implements UserDetailsService {
    private final AccountService accountService;

    public LoginUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            LoginAccountInfo accountInfo = accountService.getLoginAccount(username);
            return new LoginUserDetails(accountInfo, List.of(createGrantedAuthority(accountInfo.role())));
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    private GrantedAuthority createGrantedAuthority(Role role) {
        return new SimpleGrantedAuthority(role.name());
    }
}
