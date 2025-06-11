package com.demo.pteam.authentication.validator;

import com.demo.pteam.authentication.service.AccountService;
import org.springframework.stereotype.Component;

@Component("email")
public class EmailUniqueCheck implements UniqueCheckStrategy{
    private static final String PATTERN = "^[A-Za-z0-9._%+-]{3,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final AccountService accountService;

    public EmailUniqueCheck(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean isUnique(String email) {
        return accountService.isUniqueByEmail(email);
    }

    @Override
    public boolean isValidFormat(String value) {
        return value.matches(PATTERN);
    }
}
