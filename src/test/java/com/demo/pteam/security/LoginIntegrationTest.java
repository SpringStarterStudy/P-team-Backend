package com.demo.pteam.security;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.exception.LoginErrorCode;
import com.demo.pteam.security.login.ApiLoginFilter;
import com.demo.pteam.security.login.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class LoginIntegrationTest {
    private static final String LOGIN_REQUEST_PATH = "/api/auths/login";

    @Value("${jwt.secret}")
    public String jwtSecretKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;
    private ApiLoginFilter spyApiLoginFilter;

    @BeforeEach
    public void setUp() {
        List<Filter> filters = filterChainProxy.getFilters(LOGIN_REQUEST_PATH);
        filters.stream()
                .filter(filter -> filter instanceof ApiLoginFilter)
                .findFirst()
                .ifPresent(filter -> {
                        spyApiLoginFilter = (ApiLoginFilter) spy(filter);
                        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                .addFilter(spyApiLoginFilter)
                                .build();
                });
    }

    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {
        // given
        String username = "usertest1";
        String password = "1234567aA!";
        String testContent = creatTestContent(username, password);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGIN_REQUEST_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value("로그인 성공"))
                .andExpect(header().string("Authorization", startsWith("Bearer ")))
                .andExpect(header().string("Refresh-Token", startsWith("Bearer ")))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String accessToken = response.getHeader("Authorization").replace("Bearer ", "");
        String refreshToken = response.getHeader("Refresh-Token").replace("Bearer ", "");

        // accessToken 검증
        Claims accessTokenClaims = jwtDecode(accessToken, jwtSecretKey);
        Assertions.assertThat(Long.valueOf(accessTokenClaims.getSubject())).isEqualTo(1L);
        String roleString = accessTokenClaims.get("role", String.class);
        Assertions.assertThat(Role.valueOf(roleString)).isEqualTo(Role.ROLE_USER);
        Assertions.assertThat(accessTokenClaims.get("verified", Boolean.class)).isEqualTo(true);

        // refreshToken 검증
        Claims refreshTokenClaims = jwtDecode(refreshToken, jwtSecretKey);
        Assertions.assertThat(Long.valueOf(refreshTokenClaims.getSubject())).isEqualTo(1L);
    }

    @DisplayName("로그인 정보 불일치")
    @ParameterizedTest
    @CsvSource({
            "usertest1,11111aA!",    // 비밀번호 불일치
            "aaaaaaa,123456a!",  // 아이디 x
            "'',''",    // 빈 문자열
            ","  // null
    })
    void login_passwordMismatch(String username, String password) throws Exception {
        // given
        String testContent = creatTestContent(username, password);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGIN_REQUEST_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        LoginErrorCode errorCode = LoginErrorCode.INVALID_CREDENTIALS;
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("정지된 계정")
    @Test
    void login_suspended() throws Exception {
        // given
        String username = "usertest2";
        String password = "1234567aA!";
        String testContent = creatTestContent(username, password);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGIN_REQUEST_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        LoginErrorCode errorCode = LoginErrorCode.ACCOUNT_SUSPENDED;
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("post 요청이 아닌 경우")
    @Test
    void login_notPOST() throws Exception {
        // given
        String username = "usertest1";
        String password = "1234567aA!";
        String testContent = creatTestContent(username, password);

        // when
        ResultActions resultActions = mockMvc.perform(
                get(LOGIN_REQUEST_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        LoginErrorCode errorCode = LoginErrorCode.METHOD_NOT_ALLOWED;
        resultActions.andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("json property가 올바르지 않은 경우")
    @Test
    void login_invalidJsonProperty() throws Exception {
        // given
        String invalidJson = """
                {
                    "username": "username1",
                    "passworddd": "1234567aA!"
                }
                """;

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGIN_REQUEST_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        );

        // then
        LoginErrorCode errorCode = LoginErrorCode.INVALID_JSON_PROPERTY;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(String.format(errorCode.getMessage(), "passworddd")));
    }

    @DisplayName("요청 형식이 올바르지 않은 경우")
    @ParameterizedTest
    @ValueSource(strings = {
            "invalid request body",
            """
            {
                "invalid json"
            }
            """,
            """
            {
                "username": "username1",
                "password": "1234567aA!",
            }
            """
    })
    void login_invalidJsonFormat(String invalidJson) throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGIN_REQUEST_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        );

        // then
        LoginErrorCode errorCode = LoginErrorCode.INVALID_JSON_FORMAT;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("서버 에러 발생")
    @Test
    void login_error() throws Exception {
        // given
        String username = "usertest1";
        String password = "1234567aA!";
        String testContent = creatTestContent(username, password);

        doThrow(new AuthenticationServiceException("server error"))
                .when(spyApiLoginFilter)
                .attemptAuthentication(any(HttpServletRequest.class), any(HttpServletResponse.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                post(LOGIN_REQUEST_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        LoginErrorCode errorCode = LoginErrorCode.LOGIN_FAILED;
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    private String creatTestContent(String username, String password) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new LoginRequest(username, password));
    }

    public static Claims jwtDecode(String token, String secretKey) throws JwtException {
        return Jwts.parser()
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
