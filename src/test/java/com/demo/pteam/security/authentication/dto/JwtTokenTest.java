package com.demo.pteam.security.authentication.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenTest {
    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDU4NTMwMjEsImV4cCI6MTc0NTg1NjYyMX0.agPquXr3A56Lv6Ez7J13oD2tvwzJNjJie-TKOr9W990";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ1ODUzMDIxLCJleHAiOjE3NDY0NTc4MjF9._TgcbT4TByg2DChjojzasus9Ece6LVHr9PECZNU6YI0";
    private static final String AUTHORIZATION_HEADER = PREFIX + ACCESS_TOKEN;
    private static final String REFRESH_TOKEN_HEADER = PREFIX + REFRESH_TOKEN;

    @DisplayName("Header 정보가 입력된 경우('Bearer '로 시작할 경우)")
    @Test
    void whenPrefixExists() {
        // given
        JwtToken tokenWithBearer = JwtToken.ofBearer(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);

        // when
        Supplier<Boolean> emptyCheck = tokenWithBearer::isEmpty;
        Supplier<Boolean> prefixCheck = tokenWithBearer::hasBearerPrefix;
        Supplier<String> authHeader = tokenWithBearer::getAuthHeader;
        Supplier<String> refreshHeader = tokenWithBearer::getRefreshHeader;
        Supplier<String> accessToken = tokenWithBearer::getAccessToken;
        Supplier<String> refreshToken = tokenWithBearer::getRefreshToken;

        // then
        assertThat(emptyCheck.get()).isFalse();
        assertThat(prefixCheck.get()).isTrue();
        assertThat(authHeader.get()).isNotNull();
        assertThat(refreshHeader.get()).isNotNull();
        assertThat(authHeader.get()).isEqualTo(AUTHORIZATION_HEADER);
        assertThat(refreshHeader.get()).isEqualTo(REFRESH_TOKEN_HEADER);
        assertThat(accessToken.get()).isNotNull();
        assertThat(refreshToken.get()).isNotNull();
        assertThat(accessToken.get()).isEqualTo(ACCESS_TOKEN);
        assertThat(refreshToken.get()).isEqualTo(REFRESH_TOKEN);
    }

    @DisplayName("토큰이 입력된 경우('Bearer '로 시작하지 않는 경우)")
    @Test
    void whenPrefixNotExists() {
        // given
        JwtToken jwtToken = JwtToken.ofRaw(ACCESS_TOKEN, REFRESH_TOKEN);

        // when
        Supplier<Boolean> emptyCheck = jwtToken::isEmpty;
        Supplier<Boolean> prefixCheck = jwtToken::hasBearerPrefix;
        Supplier<String> authHeader = jwtToken::getAuthHeader;
        Supplier<String> refreshHeader = jwtToken::getRefreshHeader;
        Supplier<String> accessToken = jwtToken::getAccessToken;
        Supplier<String> refreshToken = jwtToken::getRefreshToken;

        // then
        assertThat(emptyCheck.get()).isFalse();
        assertThat(prefixCheck.get()).isFalse();
        assertThat(accessToken.get()).isNotNull();
        assertThat(refreshToken.get()).isNotNull();
        assertThat(authHeader.get()).isEqualTo(AUTHORIZATION_HEADER);
        assertThat(refreshHeader.get()).isEqualTo(REFRESH_TOKEN_HEADER);
        assertThat(accessToken.get()).isNotNull();
        assertThat(refreshToken.get()).isNotNull();
        assertThat(accessToken.get()).isEqualTo(ACCESS_TOKEN);
        assertThat(refreshToken.get()).isEqualTo(REFRESH_TOKEN);
    }

    @DisplayName("null이 입력될 경우")
    @Test
    void whenInsertNull() {
        // given
        JwtToken jwtToken = JwtToken.ofBearer(null, null);

        // when
        Supplier<Boolean> emptyCheck = jwtToken::isEmpty;
        Supplier<Boolean> prefixCheck = jwtToken::hasBearerPrefix;
        Supplier<String> authHeader = jwtToken::getAuthHeader;
        Supplier<String> refreshHeader = jwtToken::getRefreshHeader;
        Supplier<String> accessToken = jwtToken::getAccessToken;
        Supplier<String> refreshToken = jwtToken::getRefreshToken;

        // then
        assertThat(emptyCheck.get()).isTrue();
        assertThat(prefixCheck.get()).isFalse();
        assertThat(authHeader.get()).isNotNull();
        assertThat(refreshHeader.get()).isNotNull();
        assertThat(authHeader.get()).isEmpty();
        assertThat(refreshHeader.get()).isEmpty();
        assertThat(accessToken.get()).isEmpty();
        assertThat(refreshToken.get()).isEmpty();
    }
}