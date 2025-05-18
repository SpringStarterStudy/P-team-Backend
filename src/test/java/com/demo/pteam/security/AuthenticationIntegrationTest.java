package com.demo.pteam.security;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.principal.UserPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class AuthenticationIntegrationTest {
    /* accessToken payload
    {
        "sub": "1",
        "role": "ROLE_USER",
        "verified": true,
        "iat": 1747207699,
        "exp": 1747211299
    }
    */
    /* refreshToken payload
    {
        "sub": "1",
        "iat": 1747207700,
        "exp": 1747812500
    }
    */
    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.M2IjaJJCfnV7Eheijp72nKtVlL1pgkghNr-Zc1i6Oks";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.NXnMg9s2NpZFIgf6EmRdGpC9qyXDOWGbRklF39vOLBg";
    private static final String AUTHORIZATION_HEADER = PREFIX + ACCESS_TOKEN;
    private static final String REFRESH_TOKEN_HEADER = PREFIX + REFRESH_TOKEN;

    private static final String REQUEST_PATH = "/api/members";  // 사용자 인증이 필요한 경로

    private static final Date NOW = createDate(2025, 5, 14, 16, 28, 20);

    @Value("${jwt.secret}")
    public String jwtSecretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(filterChainProxy)
                .build();
    }

    @DisplayName("인증")
    @Test
    void authentication() throws Exception {
        // given
        HttpHeaders headers = getHttpHeaders(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        when(jwtProvider.parseClaims(anyString()))
                .thenAnswer(invocation -> {
                    String token = invocation.getArgument(0);
                    return parseClaims(token, NOW);
                });

        // when
        ResultActions resultActions = mockMvc.perform(
                get(REQUEST_PATH).headers(headers)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(header().string("Authorization", AUTHORIZATION_HEADER))
                .andExpect(header().string("Refresh-Token", REFRESH_TOKEN_HEADER));
    }

    @DisplayName("인증 필수 - 토큰 x")
    @Test
    void authenticationTokenIsMissing_whenAuthenticationRequired() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get(REQUEST_PATH));

        // then
        ErrorCode errorCode = AuthenticationErrorCode.NOT_AUTHENTICATED;
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("인증 필수 x - 토큰 x")
    @Test
    void authenticationTokenIsMissing_whenAuthenticationNotRequired() throws Exception {
        // given
        String path = "/";

        // when
        ResultActions resultActions = mockMvc.perform(get(path));

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("token 재발급 - accessToken 만료, refreshToken 유효")
    @Test
    void reissueToken_whenExpiredAccessToken() throws Exception {
        // given
        Date now = createDate(2025, 5, 17, 0, 0, 0);
        HttpHeaders headers = getHttpHeaders(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        when(jwtProvider.parseClaims(anyString()))
                .thenAnswer(invocation -> {
                    String token = invocation.getArgument(0);
                    return parseClaims(token, now);
                });
        when(jwtProvider.generateAccessToken(any(UserPrincipal.class)))
                .thenAnswer(invocation -> {
                    UserPrincipal principal = invocation.getArgument(0);
                    return generateAccessToken(principal, now);
                });
        when(jwtProvider.generateRefreshToken(any(UserPrincipal.class)))
                .thenAnswer(invocation -> {
                    UserPrincipal principal = invocation.getArgument(0);
                    return generateRefreshToken(principal, now);
                });

        // when
        ResultActions resultActions = mockMvc.perform(
                get(REQUEST_PATH).headers(headers)
        );

        // then
        String expectedAuthorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDc0MDc2MDAsImV4cCI6MTc0NzQxMTIwMH0.njsPH05b9znVXRA1bIVR1yMDW5W4Uc5veEbYJoBJO6g";
        String expectedRefreshTokenHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3NDA3NjAwLCJleHAiOjE3NDgwMTI0MDB9.JzK0M_e6ESbM4--nRcg8Z92kR7SvML9oPBCrAq_3eMs";
        resultActions.andExpect(status().isOk())
                .andExpect(header().string("Authorization", expectedAuthorizationHeader))
                .andExpect(header().string("Refresh-Token", expectedRefreshTokenHeader));
    }

    @DisplayName("token 만료")
    @Test
    void expiredAllToken() throws Exception {
        // given
        Date now = createDate(2025, 5, 31, 0, 0, 0);
        HttpHeaders headers = getHttpHeaders(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        when(jwtProvider.parseClaims(anyString()))
                .thenAnswer(invocation -> {
                    String token = invocation.getArgument(0);
                    return parseClaims(token, now);
                });

        // when
        ResultActions resultActions = mockMvc.perform(
                get(REQUEST_PATH).headers(headers)
        );

        // then
        ErrorCode errorCode = AuthenticationErrorCode.INVALID_AUTHENTICATION;
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("인증 정보가 유효하지 않은 경우")
    @ParameterizedTest
    @CsvSource({
            // 정지된 계정
            PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.ECLFpWPv4zCMK23ePHIDGRcM90Xbo96_rylhh_85R4M," +
                    PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.t_haw0rj8zjwocm2SwOd9JV6Lo8FDOMCYZh-ULoISPw",
            // 사용자 정보 x
            PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDAiLCJyb2xlIjoiUk9MRV9VU0VSIiwidmVyaWZpZWQiOnRydWUsImlhdCI6MTc0NzIwNzY5OSwiZXhwIjoxNzQ3MjExMjk5fQ.SSgiqa6srmV0LqqmvB5h80Y3MkI7dEQTkQrW1pRfFuY," +
                    PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDAiLCJpYXQiOjE3NDcyMDc3MDAsImV4cCI6MTc0NzgxMjUwMH0.rKQUzJKm6H1syrdFQEQ1WPvHHCSeWIhEXYHNhfiypsw",
            // 유효하지 않은 형식의 header
            ACCESS_TOKEN + "," + REFRESH_TOKEN,
            // 유효하지 않은 토큰
            PREFIX + "asdfg," + PREFIX + "sadf"
    })
    void invalidAuthentication(String authorizationHeader, String refreshTokenHeader) throws Exception {
        // given
        Date now = createDate(2025, 5, 17, 0, 0, 0);
        HttpHeaders headers = getHttpHeaders(authorizationHeader, refreshTokenHeader);
        when(jwtProvider.parseClaims(anyString()))
                .thenAnswer(invocation -> {
                    String token = invocation.getArgument(0);
                    return parseClaims(token, now);
                });

        // when
        ResultActions resultActions = mockMvc.perform(
                get(REQUEST_PATH).headers(headers)
        );

        // then
        ErrorCode errorCode = AuthenticationErrorCode.INVALID_AUTHENTICATION;
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    private static Date createDate(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return Date.from(
                LocalDateTime.of(year, month, dayOfMonth, hour, minute, second)
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toInstant()
        );
    }

    private static HttpHeaders getHttpHeaders(String authHeader, String refreshHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("Refresh-Token", refreshHeader);
        return headers;
    }

    public String generateAccessToken(UserPrincipal principal, Date now) {
        String sub = String.valueOf(principal.id());
        Map<String, Object> claims = objectMapper.convertValue(principal, new TypeReference<>() {});
        return TestJwtUtils.encode(sub, claims, jwtSecretKey, accessTokenExpiration, now);
    }

    public String generateRefreshToken(UserPrincipal principal, Date now) {
        String sub = String.valueOf(principal.id());
        return TestJwtUtils.encode(sub, jwtSecretKey, refreshTokenExpiration, now);
    }

    public Claims parseClaims(String token, Date now) throws JwtException {
        return TestJwtUtils.decode(token, jwtSecretKey, now);
    }

    static class TestJwtUtils {
        public static String encode(String subject, Map<String, Object> claims, String secretKey, Long expirationMillis, Date now) {
            Date expiration = new Date(now.getTime() + expirationMillis);

            return Jwts.builder()
                    .subject(subject)
                    .claims(claims)
                    .issuedAt(now)
                    .expiration(expiration)
                    .signWith(createSigningKey(secretKey))
                    .compact();
        }

        public static String encode(String subject, String secretKey, Long expirationMillis, Date now) {
            Date expiration = new Date(now.getTime() + expirationMillis);

            return Jwts.builder()
                    .subject(subject)
                    .issuedAt(now)
                    .expiration(expiration)
                    .signWith(createSigningKey(secretKey))
                    .compact();
        }

        public static Claims decode(String token, String secretKey, Date now) throws JwtException {
            return Jwts.parser()
                    .clock(() -> now)
                    .verifyWith(createSigningKey(secretKey))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }

        private static SecretKey createSigningKey(String secretKey) {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }
}
