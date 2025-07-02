package com.demo.pteam.authentication.validator;

import com.demo.pteam.authentication.service.AccountService;
import org.springframework.stereotype.Component;

@Component("username")
public class UsernameUniqueCheck implements UniqueCheckStrategy {
    private static final String PATTERN = "^[a-zA-Z0-9]{4,12}$";
    private final AccountService accountService;

    public UsernameUniqueCheck(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean isUnique(String username) {
        return accountService.isUniqueByUsername(username);
    }

    @Override
    public boolean isValidFormat(String value) {
        return value.matches(PATTERN);
    }
}
