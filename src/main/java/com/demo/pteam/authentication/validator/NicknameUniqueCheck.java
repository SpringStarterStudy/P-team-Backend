package com.demo.pteam.authentication.validator;

import com.demo.pteam.authentication.service.AccountService;
import org.springframework.stereotype.Component;

@Component("nickname")
public class NicknameUniqueCheck implements UniqueCheckStrategy {
    private final AccountService accountService;

    public NicknameUniqueCheck(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean isUnique(String nickname) {
        return accountService.isUniqueNickname(nickname);
    }
}
