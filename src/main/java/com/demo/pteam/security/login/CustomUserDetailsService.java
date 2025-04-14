package com.demo.pteam.security.login;

import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.login.dto.LoginAccountInfo;
import com.demo.pteam.security.principal.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {
    private final AccountService accountService;

    public CustomUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            LoginAccountInfo account = accountService.getLoginAccount(username);
            return new CustomUserDetails(account, List.of(createGrantedAuthority(account)));
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    private GrantedAuthority createGrantedAuthority(LoginAccountInfo localAccount) {
        return new SimpleGrantedAuthority(localAccount.role().name());
    }
}
