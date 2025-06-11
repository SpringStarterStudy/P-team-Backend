package com.demo.pteam.authentication;

import com.demo.pteam.authentication.controller.dto.SignupRequest;
import com.demo.pteam.global.exception.GlobalErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class SignupIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(filterChainProxy)
                .build();
    }

    @DisplayName("회원가입 성공")
    @Test
    void signup() throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value("회원가입 성공"))
                .andExpect(jsonPath("data.username").value(testSignupInfo.username()))
                .andExpect(jsonPath("data.role").value(testSignupInfo.role()))
                .andExpect(jsonPath("data.email").value(testSignupInfo.email()))
                .andExpect(jsonPath("data.name").value(testSignupInfo.name()))
                .andExpect(jsonPath("data.nickname").value(testSignupInfo.nickname()));
    }

    @DisplayName("아이디 중복")
    @Test
    void usernameConflict() throws Exception {
        // given
        String existingUsername = "usertest1";
        SignupRequest testSignupInfo = new SignupRequest(
                existingUsername,
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("username: 이미 존재하는 아이디입니다."));
    }

    @DisplayName("아이디 null")
    @Test
    void usernameNull() throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                null,
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("username: 아이디를 입력해주세요."));
    }

    @DisplayName("아이디 검증 실패")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
            "username?",    // 특수문자 사용
            "닉네임입력",    // 한글 사용
            "a",     // 4자리 미만
            "abcdefghijklm"     // 12자리 초과
    })
    void invalidUsername(String invalidUsername) throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                invalidUsername,
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("username: 아이디는 4~12자리 영문, 숫자만 가능합니다."));
    }

    @DisplayName("비밀번호 null")
    @Test
    void passwordNull() throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                null,
                null,
                "user",
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message")
                        .value("password: 비밀번호를 입력해주세요."));
    }

    @DisplayName("비밀번호 검증 실패")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
            "aab123!",    // 8자리 미만
            "abcdefghijk123456789!",    // 20자리 초과
            "abcdefgh",     // 영문만 입력
            "12345678",     // 숫자만 입력
            "!@#$%^&*",     // 특수문자만 입력
            "abcd1234",     // 영문+숫자 입력
            "abcd!@#$",     // 영문+특수문자 입력
            "1234!@#$"     // 숫자+특수문자 입력
    })
    void invalidPassword(String invalidPassword) throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                invalidPassword,
                invalidPassword,
                "user",
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message")
                        .value("password: 비밀번호는 8~20자 이상으로 영문, 숫자, 특수문자 각각 1가지 이상 사용하여 조합해주세요."));
    }

    @DisplayName("비밀번호 불일치")
    @Test
    void passwordMismatch() throws Exception {
        // given
        String password = "1234567aA!!";
        String passwordConfirm = "1234567aA!";
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                password,
                passwordConfirm,
                "user",
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("비밀번호가 일치하지 않습니다."));
    }

    @DisplayName("역할 null")
    @Test
    void roleNull() throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                null,
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("role: 역할를 입력해주세요."));
    }

    @DisplayName("유효하지 않은 역할")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = "anonymous")
    void invalidRole(String invalidRole) throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                invalidRole,
                "user@test.com",
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("role: user 또는 trainer만 가능합니다."));
    }

    @DisplayName("이메일 중복")
    @Test
    void emailConflict() throws Exception {
        // given
        String existingEmail = "usertest1@gmail.com";
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                existingEmail,
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("email: 이미 존재하는 이메일입니다."));
    }

    @DisplayName("이메일 null")
    @Test
    void emailNull() throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                null,
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("email: 이메일을 입력해주세요."));
    }

    @DisplayName("이메일 size 검증 실패")
    @ParameterizedTest
    @ValueSource(strings = {
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
                    "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
                    "abcdefghijklmnopqrstuvwxyzabcdefghijkl@test.com"   // 254자리 초과
    })
    void invalidEmailSize(String invalidEmail) throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                invalidEmail,
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("email: 이메일은 8자리 이상, 254자리 미만만 가능합니다."));
    }

    @DisplayName("이메일 형식 검증 실패")
    @ParameterizedTest
    @ValueSource(strings = {
            "ab@test.com",  // 이메일 아이디 3자리 미만
            "abc@test.c",   // 유효하지 않은 이메일 도메인 형식
            "abc@test.",   // 유효하지 않은 이메일 도메인 형식
            "abc@test",   // 유효하지 않은 이메일 도메인 형식
            "abc@.com"    // 유효하지 않은 이메일 도메인 형식
    })
    void invalidEmailPattern(String invalidEmail) throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                invalidEmail,
                "이름",
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("email: 이메일 형식이 맞지 않습니다."));
    }

    @DisplayName("이름 null")
    @Test
    void nameNull() throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                null,
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("name: 이름을 입력해주세요."));
    }

    @DisplayName("이름 검증 실패")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
            "ab@test.com",  // 이메일 아이디 3자리 미만
            "abc@test.c",   // 유효하지 않은 이메일 도메인 형식
            "abc@test.",   // 유효하지 않은 이메일 도메인 형식
            "abc@test",   // 유효하지 않은 이메일 도메인 형식
            "abc@.c"    // 유효하지 않은 이메일 도메인 형식
    })
    void invalidName(String invalidName) throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                invalidName,
                "닉네임"
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("name: 이름은 2~10자 이상의 한글 또는 영어로 입력해주세요."));
    }

    @DisplayName("닉네임 중복")
    @Test
    void nicknameConflict() throws Exception {
        // given
        String existingNickname = "회원닉네임1";
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                "이름",
                existingNickname
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("nickname: 이미 존재하는 닉네임입니다."));
    }

    @DisplayName("닉네임 null")
    @Test
    void nicknameNull() throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                "이름",
                null
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("nickname: 닉네임을 입력해주세요."));
    }

    @DisplayName("닉네임 검증 실패")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
            "가",    // 2글자 미만
            "abcdefghijk",      // 10글자 초과
            "이름?",  // 특수문자
    })
    void invalidNickname(String invalidNickname) throws Exception {
        // given
        SignupRequest testSignupInfo = new SignupRequest(
                "username3",
                "1234567aA!",
                "1234567aA!",
                "user",
                "user@test.com",
                "이름",
                invalidNickname
        );
        String testContent = objectMapper.writeValueAsString(testSignupInfo);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auths/signup")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testContent)
        );

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.VALIDATION_EXCEPTION;
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(errorCode.getStatus().value()))
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value("nickname: 닉네임은 2~10자 이상의 한글, 영어, 숫자만 가능합니다."));
    }
}
