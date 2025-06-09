package com.demo.pteam.authentication.validator;

import com.demo.pteam.authentication.service.AccountService;
import org.springframework.stereotype.Component;

@Component("email")
public class EmailUniqueCheck implements UniqueCheckStrategy{
    private final AccountService accountService;

    public EmailUniqueCheck(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean isUnique(String email) {
        return accountService.isUniqueByEmail(email);
    }
}
