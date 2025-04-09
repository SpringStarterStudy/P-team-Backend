package com.demo.pteam.security.login;

import com.demo.pteam.authentication.exception.UserNotFoundException;
import com.demo.pteam.authentication.service.AccountService;
import com.demo.pteam.security.principal.CustomUserDetails;
import com.demo.pteam.security.login.dto.LocalAccountDto;
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
            LocalAccountDto localAccount = accountService.getLocalAccount(username);
            return new CustomUserDetails(localAccount, List.of(createGrantedAuthority(localAccount)));
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    private GrantedAuthority createGrantedAuthority(LocalAccountDto localAccount) {
        return new SimpleGrantedAuthority(localAccount.role().name());
    }
}
