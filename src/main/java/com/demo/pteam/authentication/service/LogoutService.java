package com.demo.pteam.authentication.service;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import com.demo.pteam.security.jwt.TokenBlackList;
import com.demo.pteam.security.jwt.TokenData;
import com.demo.pteam.security.jwt.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private static final String PREFIX = "Bearer ";

    private final TokenStore tokenStore;
    private final TokenBlackList tokenBlackList;

    @PreAuthorize("isAuthenticated() and authentication.principal.id == #accountId")
    public void logout(Long accountId, String refreshToken) {
        String rawRefreshToken = removeBearerPrefix(refreshToken);
        if (tokenStore.isInvalid(accountId, rawRefreshToken)) {
            throw new ApiException(AuthenticationErrorCode.INVALID_AUTHENTICATION);
        }
        saveBlackList(accountId, rawRefreshToken);
    }

    private String removeBearerPrefix(String token) {
        return token.startsWith(PREFIX)
                ? token.substring(PREFIX.length())
                : token;
    }

    private void saveBlackList(Long accountId, String rawRefreshToken) {
        TokenData tokenData = tokenStore.findByAccountId(accountId).orElseThrow(() ->
                new ApiException(AuthenticationErrorCode.INVALID_AUTHENTICATION));
        tokenStore.delete(accountId);   // 저장된 토큰 삭제
        tokenBlackList.save(rawRefreshToken, "logout", tokenData.expirationMillis());  // 블랙리스트 저장
    }
}
