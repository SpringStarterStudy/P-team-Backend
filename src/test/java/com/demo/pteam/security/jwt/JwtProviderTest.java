package com.demo.pteam.security.jwt;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
    @InjectMocks
    private JwtProvider jwtProvider;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String testSecretKey = "CQakf5359nUg2eTfl8lF4ceO7l+g8usgLW10BfVBFHw=";
    private static final long testAccessTokenExpiration = 3600000L;
    private static final long testRefreshTokenExpiration = 604800000L;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtProvider, "secretKey", testSecretKey);
        ReflectionTestUtils.setField(jwtProvider, "accessTokenExpiration", testAccessTokenExpiration);
        ReflectionTestUtils.setField(jwtProvider, "refreshTokenExpiration", testRefreshTokenExpiration);
    }

    @Test
    void generateAccessToken() {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            UserPrincipal testPrincipal = new UserPrincipal(1L, Role.ROLE_USER, true);
            Map<String, Object> claims = objectMapper.convertValue(testPrincipal, new TypeReference<>() {});
            String expectedAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDQ4OTI1NzgsImV4cCI6MTc0NDg5NjE3OH0.TrNsmciSJM_BmmF20o_nVua4s7pnokHmMUzhMZIej5Q";
            utilities.when(() -> JwtUtils.encode("1", claims, testSecretKey, testAccessTokenExpiration))
                    .thenReturn(expectedAccessToken);

            // when
            String accessToken = jwtProvider.generateAccessToken(testPrincipal);

            // then
            assertThat(accessToken).isEqualTo(expectedAccessToken);
        }
    }

    @Test
    void generateRefreshToken() {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            UserPrincipal testPrincipal = new UserPrincipal(1L, Role.ROLE_USER, true);
            String expectedRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ0ODkyNTc4LCJleHAiOjE3NDQ4OTYxNzh9.cxYBI2knIN2tYndSZpzEwSOEAktz5u5DEw7n3AkLCqM";
            utilities.when(() -> JwtUtils.encode("1", testSecretKey, testRefreshTokenExpiration))
                    .thenReturn(expectedRefreshToken);

            // when
            String accessToken = jwtProvider.generateRefreshToken(testPrincipal);

            // then
            assertThat(accessToken).isEqualTo(expectedRefreshToken);
        }
    }

    @Test
    void obtainPrincipal() {
        // TODO: 나중에 사용할 때 작성 예정
    }
}