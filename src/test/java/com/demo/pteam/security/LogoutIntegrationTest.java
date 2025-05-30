package com.demo.pteam.security;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import com.demo.pteam.security.jwt.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class LogoutIntegrationTest {
    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.M2IjaJJCfnV7Eheijp72nKtVlL1pgkghNr-Zc1i6Oks";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.NXnMg9s2NpZFIgf6EmRdGpC9qyXDOWGbRklF39vOLBg";
    private static final String AUTHORIZATION_HEADER = PREFIX + ACCESS_TOKEN;
    private static final String REFRESH_TOKEN_HEADER = PREFIX + REFRESH_TOKEN;

    private static final String BLACK_LISTED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NjAwLCJleHAiOjE3NDc4MTI1MDB9.hV1YYOo0nHGBrBixLRxiupfeeLtMJyaXyNUTWctCQcQ";

    private static final String LOGOUT_REQUEST_PATH = "/api/auths/logout";

    private static final Date NOW = createDate(2025, 5, 14, 16, 28, 20);

    private static final long REFRESH_TOKEN_EXPIRATION = 1747812500000L;

    @Value("${jwt.secret}")
    public String jwtSecretKey;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @MockitoSpyBean
    private JwtProvider jwtProvider;

    @MockitoSpyBean
    private InMemoryTokenStore spyTokenStore;

    @MockitoSpyBean
    private InMemoryTokenBlackList spyTokenBlackList;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(filterChainProxy)
                .build();

        // 블랙리스트 등록
        BlackListedToken blackListedToken = new BlackListedToken("reissue", REFRESH_TOKEN_EXPIRATION);
        BlackListedToken spyBlackListedToken = spy(blackListedToken);
        Map<String, BlackListedToken> blackList = (Map<String, BlackListedToken>) ReflectionTestUtils.getField(spyTokenBlackList, "blackList");
        blackList.clear();
        blackList.put(BLACK_LISTED_REFRESH_TOKEN, blackListedToken);
        doReturn(NOW.getTime() > REFRESH_TOKEN_EXPIRATION).when(spyBlackListedToken).isExpired();

        // refreshToken 저장
        TokenData tokenData = new TokenData(REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION);
        TokenData spyTokenData = spy(tokenData);
        Map<Long, TokenData> store = (Map<Long, TokenData>) ReflectionTestUtils.getField(spyTokenStore, "store");
        store.clear();
        store.put(1L, spyTokenData);
        doReturn(NOW.getTime() > REFRESH_TOKEN_EXPIRATION).when(spyTokenData).isExpired();

        doAnswer(invocation -> {
            String token = invocation.getArgument(0);
            return jwtDecode(token, jwtSecretKey, NOW);
        }).when(jwtProvider).parseClaims(anyString());
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        // given
        HttpHeaders headers = getHttpHeaders(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGOUT_REQUEST_PATH).headers(headers)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value("로그아웃 되었습니다."))
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh-Token"));
    }

    @DisplayName("post 요청이 아닌 경우")
    @Test
    void logout_notPOST() throws Exception {
        // given
        HttpHeaders headers = getHttpHeaders(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);

        // when
        ResultActions resultActions = mockMvc.perform(
                get(LOGOUT_REQUEST_PATH).headers(headers)
        );

        // then
        AuthenticationErrorCode errorCode = AuthenticationErrorCode.METHOD_NOT_ALLOWED;
        resultActions.andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("인증 정보가 유효하지 않은 경우")
    @ParameterizedTest
    @CsvSource({
            // token 만료
            PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDYyMDc2OTksImV4cCI6MTc0NjIxMTI5OX0.hKvQt7OnZm9PrKwChdLoV2AOu8dl986KkadBGWl1Rwk," +
                    PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ2MjA3NzAwLCJleHAiOjE3NDY4MTI1MDB9.RM0vvy-wmC4h85xCPMIdp4XZ6KJxghK7qs0-oZwsaLA",
            // 저장되지 않은 refreshToken
            AUTHORIZATION_HEADER + "," +
                    PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI2MDB9.NUGUY8psTrIpnafnZV0qzrBqbQqJAotZCdsZEQAn3Xw",
            // 블랙리스트에 등록된 refreshToken
            AUTHORIZATION_HEADER + "," +
                    PREFIX + BLACK_LISTED_REFRESH_TOKEN,
            // 유효하지 않은 형식의 header
            ACCESS_TOKEN + "," + REFRESH_TOKEN,
            // 유효하지 않은 토큰
            PREFIX + "asdfg," + PREFIX + "sadf"
    })
    void logout_invalidAuthentication(String authorizationHeader, String refreshTokenHeader) throws Exception {
        // given
        HttpHeaders headers = getHttpHeaders(authorizationHeader, refreshTokenHeader);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGOUT_REQUEST_PATH).headers(headers)
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
