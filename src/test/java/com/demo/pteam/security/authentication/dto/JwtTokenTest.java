package com.demo.pteam.security.authentication.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenTest {
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDU4NTMwMjEsImV4cCI6MTc0NTg1NjYyMX0.agPquXr3A56Lv6Ez7J13oD2tvwzJNjJie-TKOr9W990";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ1ODUzMDIxLCJleHAiOjE3NDY0NTc4MjF9._TgcbT4TByg2DChjojzasus9Ece6LVHr9PECZNU6YI0";

    @DisplayName("입력된 값에 prefix가 존재할 경우")
    @Test
    void whenPrefixExists() {
        // given
        String prefix = "Bearer ";
        String testAccessToken = prefix + ACCESS_TOKEN;
        String testRefreshToken = prefix + REFRESH_TOKEN;
        JwtToken jwtToken = new JwtToken(testAccessToken, testRefreshToken);

        // when
        Supplier<Boolean> emptyCheck = jwtToken::isEmpty;
        Supplier<Boolean> prefixCheck = jwtToken::hasPrefix;
        Supplier<String> accessToken = jwtToken::accessToken;
        Supplier<String> refreshToken = jwtToken::refreshToken;
        Supplier<String> extractAccessToken = jwtToken::extractAccessToken;
        Supplier<String> extractRefreshToken = jwtToken::extractRefreshToken;

        // then
        assertThat(emptyCheck.get()).isFalse();
        assertThat(prefixCheck.get()).isTrue();
        assertThat(accessToken.get()).isNotNull();
        assertThat(refreshToken.get()).isNotNull();
        assertThat(accessToken.get()).isEqualTo(testAccessToken);
        assertThat(refreshToken.get()).isEqualTo(testRefreshToken);
        assertThat(extractAccessToken.get()).isEqualTo(ACCESS_TOKEN);
        assertThat(extractRefreshToken.get()).isEqualTo(REFRESH_TOKEN);
    }

    @DisplayName("입력된 값에 prefix가 존재하지 않을 경우")
    @Test
    void whenPrefixNotExists() {
        // given
        String testAccessToken = ACCESS_TOKEN;
        String testRefreshToken = REFRESH_TOKEN;
        JwtToken jwtToken = new JwtToken(testAccessToken, testRefreshToken);

        // when
        Supplier<Boolean> emptyCheck = jwtToken::isEmpty;
        Supplier<Boolean> prefixCheck = jwtToken::hasPrefix;
        Supplier<String> accessToken = jwtToken::accessToken;
        Supplier<String> refreshToken = jwtToken::refreshToken;
        Supplier<String> extractAccessToken = jwtToken::extractAccessToken;
        Supplier<String> extractRefreshToken = jwtToken::extractRefreshToken;

        // then
        assertThat(emptyCheck.get()).isFalse();
        assertThat(prefixCheck.get()).isFalse();
        assertThat(accessToken.get()).isNotNull();
        assertThat(refreshToken.get()).isNotNull();
        assertThat(accessToken.get()).isEqualTo(testAccessToken);
        assertThat(refreshToken.get()).isEqualTo(testRefreshToken);
        assertThat(extractAccessToken.get()).isEqualTo(ACCESS_TOKEN);
        assertThat(extractRefreshToken.get()).isEqualTo(REFRESH_TOKEN);
    }

    @DisplayName("null이 입력될 경우")
    @Test
    void whenInsertNull() {
        // given
        JwtToken jwtToken = new JwtToken(null, null);

        // when
        Supplier<Boolean> emptyCheck = jwtToken::isEmpty;
        Supplier<Boolean> prefixCheck = jwtToken::hasPrefix;
        Supplier<String> accessToken = jwtToken::accessToken;
        Supplier<String> refreshToken = jwtToken::refreshToken;
        Supplier<String> extractAccessToken = jwtToken::extractAccessToken;
        Supplier<String> extractRefreshToken = jwtToken::extractRefreshToken;

        // then
        assertThat(emptyCheck.get()).isTrue();
        assertThat(prefixCheck.get()).isFalse();
        assertThat(accessToken.get()).isNotNull();
        assertThat(refreshToken.get()).isNotNull();
        assertThat(accessToken.get()).isEmpty();
        assertThat(refreshToken.get()).isEmpty();
        assertThat(extractAccessToken.get()).isEmpty();
        assertThat(extractRefreshToken.get()).isEmpty();
    }
}