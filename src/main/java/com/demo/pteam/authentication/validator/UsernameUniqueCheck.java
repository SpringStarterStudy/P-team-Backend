package com.demo.pteam.authentication.validator;

import com.demo.pteam.authentication.service.AccountService;
import org.springframework.stereotype.Component;

@Component("username")
public class UsernameUniqueCheck implements UniqueCheckStrategy {
    private final AccountService accountService;

    public UsernameUniqueCheck(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean isUnique(String username) {
        return accountService.isUniqueByUsername(username);
    }
}
