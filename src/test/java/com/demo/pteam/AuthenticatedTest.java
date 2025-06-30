package com.demo.pteam;

import com.demo.pteam.security.jwt.InMemoryTokenStore;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.jwt.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public abstract class AuthenticatedTest {
    protected static final String PREFIX = "Bearer ";
    protected static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.M2IjaJJCfnV7Eheijp72nKtVlL1pgkghNr-Zc1i6Oks";
    protected static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.NXnMg9s2NpZFIgf6EmRdGpC9qyXDOWGbRklF39vOLBg";
    protected static final String AUTHORIZATION_HEADER = PREFIX + ACCESS_TOKEN;
    protected static final String REFRESH_TOKEN_HEADER = PREFIX + REFRESH_TOKEN;

    private static final Date NOW = createDate(2025, 5, 14, 16, 28, 20);

    private static final long REFRESH_TOKEN_EXPIRATION = 1747812500000L;

    @Value("${jwt.secret}")
    protected String jwtSecretKey;

    @Autowired
    private WebApplicationContext context;
    protected MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @MockitoSpyBean
    private JwtProvider jwtProvider;

    @MockitoSpyBean
    private InMemoryTokenStore spyTokenStore;

    @BeforeEach
    public void authenticationSetUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(filterChainProxy)
                .build();

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

    public static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", AUTHORIZATION_HEADER);
        headers.set("Refresh-Token", REFRESH_TOKEN_HEADER);
        return headers;
    }

    public static HttpHeaders getHttpHeaders(String authHeader, String refreshHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("Refresh-Token", refreshHeader);
        return headers;
    }

    private static Date createDate(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return Date.from(
                LocalDateTime.of(year, month, dayOfMonth, hour, minute, second)
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toInstant()
        );
    }

    private static Claims jwtDecode(String token, String secretKey, Date now) throws JwtException {
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
