package com.demo.pteam.authentication.validator;

import com.demo.pteam.authentication.service.AccountService;
import org.springframework.stereotype.Component;

@Component("nickname")
public class NicknameUniqueCheck implements UniqueCheckStrategy {
    private static final String PATTERN = "^[가-힣a-zA-Z0-9]{2,10}$";
    private final AccountService accountService;

    public NicknameUniqueCheck(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean isUnique(String nickname) {
        return accountService.isUniqueNickname(nickname);
    }

    @Override
    public boolean isValidFormat(String value) {
        return value.matches(PATTERN);
    }
}
