package com.demo.pteam.security;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.security.jwt.JwtService;
import com.demo.pteam.security.authentication.dto.JwtToken;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.jwt.TokenData;
import com.demo.pteam.security.jwt.TokenStore;
import com.demo.pteam.security.principal.UserPrincipal;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;
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

    private static final long REFRESH_TOKEN_EXPIRATION = 1747812500000L;

    @Value("${jwt.secret}")
    public String jwtSecretKey;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTTL;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @MockitoSpyBean
    private JwtService jwtService;

    @MockitoSpyBean
    private JwtProvider jwtProvider;

    @MockitoSpyBean
    private TokenStore tokenStore;

    private TokenData spyTokenData;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(filterChainProxy)
                .build();

        TokenData tokenData = new TokenData(REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION);
        spyTokenData = spy(tokenData);
        Map<Long, TokenData> store = (Map<Long, TokenData>) ReflectionTestUtils.getField(tokenStore, "store");
        store.put(1L, spyTokenData);

        doReturn(NOW.getTime() > REFRESH_TOKEN_EXPIRATION).when(spyTokenData).isExpired();

        doAnswer(invocation -> {
            String token = invocation.getArgument(0);
            return jwtDecode(token, jwtSecretKey, NOW);
        }).when(jwtProvider).parseClaims(anyString());

        doAnswer(invocation -> {
            UserPrincipal principal = invocation.getArgument(0);
            String accessToken = jwtProvider.generateAccessToken(principal, NOW);
            String refreshToken = jwtProvider.generateRefreshToken(principal, NOW);
            tokenStore.save(principal.id(), refreshToken, NOW.getTime() + refreshTokenTTL);
            return JwtToken.ofRaw(accessToken, refreshToken);
        }).when(jwtService).createJwtToken(any(UserPrincipal.class));
    }

    @DisplayName("인증")
    @Test
    void authentication() throws Exception {
        // given
        HttpHeaders headers = getHttpHeaders(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);

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
        String expiredAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcxOTc2OTksImV4cCI6MTc0NzIwMTI5OX0.N_rIOLOXpRwJePbV2rXXldzZCZpVTPodV1HtAg1pTo4";
        String authorizationHeader = PREFIX + expiredAccessToken;
        HttpHeaders headers = getHttpHeaders(authorizationHeader, REFRESH_TOKEN_HEADER);

        // when
        ResultActions resultActions = mockMvc.perform(
                get(REQUEST_PATH).headers(headers)
        );

        // then
        String expectedAuthorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc3MDAsImV4cCI6MTc0NzIxMTMwMH0.rDK2OWyTgu5XWEfqz5NOszPmxSOGinhMfaonuWbbZz4";
        String expectedRefreshTokenHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.NXnMg9s2NpZFIgf6EmRdGpC9qyXDOWGbRklF39vOLBg";
        resultActions.andExpect(status().isOk())
                .andExpect(header().string("Authorization", expectedAuthorizationHeader))
                .andExpect(header().string("Refresh-Token", expectedRefreshTokenHeader));
    }

    @DisplayName("token 만료")
    @Test
    void expiredAllToken() throws Exception {
        // given
        String expiredAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDYyMDc2OTksImV4cCI6MTc0NjIxMTI5OX0.hKvQt7OnZm9PrKwChdLoV2AOu8dl986KkadBGWl1Rwk";
        String expiredRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ2MjA3NzAwLCJleHAiOjE3NDY4MTI1MDB9.RM0vvy-wmC4h85xCPMIdp4XZ6KJxghK7qs0-oZwsaLA";
        String authorizationHeader = PREFIX + expiredAccessToken;
        String refreshTokenHeader = PREFIX + expiredRefreshToken;

        HttpHeaders headers = getHttpHeaders(authorizationHeader, refreshTokenHeader);

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
            PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcxOTc2OTksImV4cCI6MTc0NzIwMTI5OX0.6zGtTw13sWHLQS2RDTdjFFpn2YIcU269Vvas6kKT52g," +
                    PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.t_haw0rj8zjwocm2SwOd9JV6Lo8FDOMCYZh-ULoISPw",
            // 사용자 정보 x
            PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDAiLCJyb2xlIjoiUk9MRV9VU0VSIiwidmVyaWZpZWQiOnRydWUsImlhdCI6MTc0NzE5NzY5OSwiZXhwIjoxNzQ3MjAxMjk5fQ.D_4lca0W-wCr-jRP-BgRTV8mOq2gLzKyLLMz5yU_bx4," +
                    PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDAiLCJpYXQiOjE3NDcyMDc3MDAsImV4cCI6MTc0NzgxMjUwMH0.rKQUzJKm6H1syrdFQEQ1WPvHHCSeWIhEXYHNhfiypsw",
            // 유효하지 않은 형식의 header
            ACCESS_TOKEN + "," + REFRESH_TOKEN,
            // 유효하지 않은 토큰
            PREFIX + "asdfg," + PREFIX + "sadf"
    })
    void invalidAuthentication(String authorizationHeader, String refreshTokenHeader) throws Exception {
        // given
        HttpHeaders headers = getHttpHeaders(authorizationHeader, refreshTokenHeader);

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

    public static Claims jwtDecode(String token, String secretKey, Date now) throws JwtException {
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
