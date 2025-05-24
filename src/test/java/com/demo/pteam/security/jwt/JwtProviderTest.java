package com.demo.pteam.security.jwt;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
    @InjectMocks
    private JwtProvider jwtProvider;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private Claims claims;

    private static final String TEST_SECRET_KEY = "CQakf5359nUg2eTfl8lF4ceO7l+g8usgLW10BfVBFHw=";
    private static final long TEST_ACCESS_TOKEN_TTL = 3600000L;
    private static final long TEST_REFRESH_TOKEN_TTL = 604800000L;
    private static final Date NOW = Date.from(
            LocalDateTime.of(2025, 4, 17, 21, 22, 58)
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toInstant()
    );

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtProvider, "secretKey", TEST_SECRET_KEY);
        ReflectionTestUtils.setField(jwtProvider, "accessTokenTTL", TEST_ACCESS_TOKEN_TTL);
        ReflectionTestUtils.setField(jwtProvider, "refreshTokenTTL", TEST_REFRESH_TOKEN_TTL);
    }

    @DisplayName("accessToken 생성")
    @Test
    void generateAccessToken() {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            UserPrincipal testPrincipal = new UserPrincipal(1L, Role.ROLE_USER, true);
            Map<String, Object> claims = objectMapper.convertValue(testPrincipal, new TypeReference<>() {});
            String expectedAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDQ4OTI1NzgsImV4cCI6MTc0NDg5NjE3OH0.vcl4raUrANBRCgJ1rciXit8KIhvrt5STIpvX-4HLZCU";
            utilities.when(() -> JwtUtils.encode("1", claims, TEST_SECRET_KEY, TEST_ACCESS_TOKEN_TTL, NOW))
                    .thenReturn(expectedAccessToken);

            // when
            String accessToken = jwtProvider.generateAccessToken(testPrincipal, NOW);

            // then
            assertThat(accessToken).isEqualTo(expectedAccessToken);
        }
    }

    @DisplayName("refreshToken 생성")
    @Test
    void generateRefreshToken() {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            UserPrincipal testPrincipal = new UserPrincipal(1L, Role.ROLE_USER, true);
            String expectedRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ0ODkyNTc4LCJleHAiOjE3NDQ4OTYxNzh9.3oA6KqZkPHeLXMpT5nOfXun4E5UAI5FgSCvKtTyoOjA";
            utilities.when(() -> JwtUtils.encode("1", TEST_SECRET_KEY, TEST_REFRESH_TOKEN_TTL, NOW))
                    .thenReturn(expectedRefreshToken);

            // when
            String accessToken = jwtProvider.generateRefreshToken(testPrincipal, NOW);

            // then
            assertThat(accessToken).isEqualTo(expectedRefreshToken);
        }
    }

    @DisplayName("토큰 파싱 후 Claims 반환")
    @Test
    void parseClaims() {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            /*  jwt payload
            {
                "sub": "1",
                "role": "ROLE_USER",
                "verified": true,
                "iat": 1744892578,
                "exp": 1744896178
            }
             */
            String testAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDQ4OTI1NzgsImV4cCI6MTc0NDg5NjE3OH0.vcl4raUrANBRCgJ1rciXit8KIhvrt5STIpvX-4HLZCU";
            utilities.when(() -> JwtUtils.decode(testAccessToken, TEST_SECRET_KEY)).thenReturn(claims);
            when(claims.getSubject()).thenReturn("1");
            when(claims.get("role", String.class)).thenReturn("ROLE_USER");
            when(claims.get("verified", Boolean.class)).thenReturn(true);

            // when
            Claims claims = jwtProvider.parseClaims(testAccessToken);

            // then
            assertThat(claims.getSubject()).isEqualTo("1");
            assertThat(claims.get("role", String.class)).isEqualTo("ROLE_USER");
            assertThat(claims.get("verified", Boolean.class)).isEqualTo(true);
        }
    }

    @DisplayName("토큰 파싱 후 Claims 반환 - 토큰 만료")
    @Test
    void parseClaims_expiredToken() {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            String testAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDQ4OTI1NzgsImV4cCI6MTc0NDg5NjE3OH0.vcl4raUrANBRCgJ1rciXit8KIhvrt5STIpvX-4HLZCU";
            utilities.when(() -> JwtUtils.decode(testAccessToken, TEST_SECRET_KEY)).thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));

            // when
            ThrowingCallable action = () -> jwtProvider.parseClaims(testAccessToken);

            // then
            assertThatExceptionOfType(ExpiredJwtException.class)
                    .isThrownBy(action)
                    .withMessage("JWT expired");
        }
    }

    @DisplayName("토큰 파싱 후 Claims 반환 - 유효하지 않은 토큰")
    @ParameterizedTest
    @ValueSource(strings = {
            // 유효하지 않은 서명의 JWT
            "eyJhbGciOiJIUzI1NiIsInppcCI6IkRFRiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDQ4OTI1NzgsImV4cCI6MTc0NDg5NjE3OH0.OhP13CUFY6YyzLTYd6imto74MD7x9PWz4p_zfHKkbNw",
            "sdfsdfs", // 유효하지 않은 형식
    })
    void parseClaims_invalidToken(String invalidToken) {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            utilities.when(() -> JwtUtils.decode(invalidToken, TEST_SECRET_KEY)).thenThrow(new JwtException("Invalid JWT"));

            // when
            ThrowingCallable action = () -> jwtProvider.parseClaims(invalidToken);

            // then
            assertThatExceptionOfType(JwtException.class)
                    .isThrownBy(action)
                    .withMessage("Invalid JWT");
        }
    }

    @DisplayName("토큰 파싱 후 Claims 반환 - null 또는 빈 문자열 입력")
    @ParameterizedTest
    @NullAndEmptySource
    void parseClaims_nullOrEmpty(String invalidToken) {
        try (MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class)) {
            // given
            utilities.when(() -> JwtUtils.decode(invalidToken, TEST_SECRET_KEY)).thenThrow(new IllegalArgumentException("CharSequence cannot be null or empty."));

            // when
            ThrowingCallable action = () -> jwtProvider.parseClaims(invalidToken);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(action)
                    .withMessage("CharSequence cannot be null or empty.");
        }
    }
}