package com.demo.pteam.security.jwt;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.authentication.JwtUserDetailsImpl;
import com.demo.pteam.security.authentication.JwtUserDetailsService;
import com.demo.pteam.security.authentication.dto.JwtReissueResult;
import com.demo.pteam.security.authentication.dto.JwtToken;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.jwt.exception.DisabledAccountException;
import com.demo.pteam.security.principal.PrincipalFactory;
import com.demo.pteam.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ1ODUzMDIxLCJleHAiOjE3NDY0NTc4MjF9._TgcbT4TByg2DChjojzasus9Ece6LVHr9PECZNU6YI0";

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private TokenStore tokenStore;

    @Mock
    private TokenBlackList tokenBlackList;

    @Mock
    private Claims claims;

    private static JwtAccountInfo getTestAccountInfo() {
        return new JwtAccountInfo(
                1L,
                Role.ROLE_USER,
                AccountStatus.ACTIVE
        );
    }

    private static JwtUserDetailsImpl getTestUserDetails(JwtAccountInfo accountInfo) {
        return new JwtUserDetailsImpl(
                accountInfo, List.of(new SimpleGrantedAuthority(accountInfo.role().name())));
    }

    private static UserPrincipal getTestPrincipal(JwtUserDetailsImpl testUserDetails) {
        return new UserPrincipal(
                testUserDetails.getAccount().id(),
                testUserDetails.getAccount().role(),
                testUserDetails.isUnverified()
        );
    }

    @DisplayName("jwt 재발급")
    @Test
    void reissue() {
        try(MockedStatic<PrincipalFactory> factory = mockStatic(PrincipalFactory.class)) {
            // given
            when(jwtProvider.parseClaims(REFRESH_TOKEN)).thenReturn(claims);
            when(claims.getSubject()).thenReturn(TEST_ACCOUNT_ID.toString());
            when(tokenStore.isInvalid(TEST_ACCOUNT_ID, REFRESH_TOKEN)).thenReturn(false);
            when(tokenBlackList.isBlackListed(REFRESH_TOKEN)).thenReturn(false);

            JwtAccountInfo testAccountInfo = getTestAccountInfo();
            JwtUserDetailsImpl testUserDetails = getTestUserDetails(testAccountInfo);
            when(jwtUserDetailsService.loadUserById(TEST_ACCOUNT_ID)).thenReturn(testUserDetails);

            UserPrincipal expectedPrincipal = getTestPrincipal(testUserDetails);
            factory.when(() -> PrincipalFactory.fromUser(testUserDetails))
                    .thenReturn(expectedPrincipal);

            String reissueAccessToken = "testReissueAccessToken";
            String reissueRefreshToken = "testReissueRefreshToken";
            when(tokenStore.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(Optional.of(new TokenData(reissueRefreshToken, 1716457821000L)));
            JwtService spyJwtService = spy(new JwtService(jwtProvider, jwtUserDetailsService, tokenStore, tokenBlackList));
            when(spyJwtService.createJwtToken(expectedPrincipal)).thenReturn(JwtToken.ofRaw(reissueAccessToken, reissueRefreshToken));

            // when
            JwtReissueResult reissue = spyJwtService.reissue(REFRESH_TOKEN);

            // then
            assertThat(reissue).isNotNull();
            assertThat(reissue.principal()).isNotNull();
            assertThat(reissue.principal()).isEqualTo(expectedPrincipal);
            assertThat(reissue.token()).isNotNull();
            assertThat(reissue.token().getAccessToken()).isEqualTo(reissueAccessToken);
            assertThat(reissue.token().getRefreshToken()).isEqualTo(reissueRefreshToken);
        }
    }

    @DisplayName("jwt 재발급 - 서버에서 발급한 토큰이 아닌 경우")
    @Test
    void reissue_notRegisteredRefreshToken() {
        // given
        when(jwtProvider.parseClaims(REFRESH_TOKEN)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(TEST_ACCOUNT_ID.toString());
        when(tokenStore.isInvalid(TEST_ACCOUNT_ID, REFRESH_TOKEN)).thenReturn(true);

        // when
        ThrowingCallable action = () -> jwtService.reissue(REFRESH_TOKEN);

        // then
        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(action)
                .withMessage("Invalid JWT");
    }

    @DisplayName("jwt 재발급 - 블랙리스트에 등록된 경우")
    @Test
    void reissue_registeredBlackList() {
        // given
        when(jwtProvider.parseClaims(REFRESH_TOKEN)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(TEST_ACCOUNT_ID.toString());
        when(tokenStore.isInvalid(TEST_ACCOUNT_ID, REFRESH_TOKEN)).thenReturn(false);
        when(tokenBlackList.isBlackListed(REFRESH_TOKEN)).thenReturn(true);

        // when
        ThrowingCallable action = () -> jwtService.reissue(REFRESH_TOKEN);

        // then
        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(action)
                .withMessage("Invalid JWT");
    }

    @DisplayName("jwt 재발급 - 조회된 계정이 없는 경우")
    @Test
    void reissue_unregisteredUsername() {
        // given
        /*  jwt payload
        {
            "sub": "100",
                "iat": 1745853021,
                "exp": 1746457821
        }
        */
        String testRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDAiLCJpYXQiOjE3NDU4NTMwMjEsImV4cCI6MTc0NjQ1NzgyMX0.6NGY2kUnyYW0PpxSbrk2jPV5WnhJ5stxPUDOwqfeYsU";
        Long unregisteredAccountId = 100L;
        when(jwtProvider.parseClaims(testRefreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(unregisteredAccountId.toString());
        when(tokenStore.isInvalid(unregisteredAccountId, testRefreshToken)).thenReturn(false);
        when(tokenBlackList.isBlackListed(testRefreshToken)).thenReturn(false);
        when(jwtUserDetailsService.loadUserById(unregisteredAccountId))
                .thenThrow(new UsernameNotFoundException("AccountId not found: " + unregisteredAccountId));

        // when
        ThrowingCallable action = () -> jwtService.reissue(testRefreshToken);

        // then
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(action)
                .withMessage("AccountId not found: " + unregisteredAccountId);
    }

    @DisplayName("jwt 재발급 - 조회된 계정이 정지된 경우")
    @Test
    void reissue_suspendedAccount() {
        // given
        when(jwtProvider.parseClaims(REFRESH_TOKEN)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(TEST_ACCOUNT_ID.toString());
        when(tokenStore.isInvalid(TEST_ACCOUNT_ID, REFRESH_TOKEN)).thenReturn(false);
        when(tokenBlackList.isBlackListed(REFRESH_TOKEN)).thenReturn(false);
        JwtUserDetailsImpl mockUserDetails = mock(JwtUserDetailsImpl.class);
        when(mockUserDetails.isSuspended()).thenReturn(true);
        when(jwtUserDetailsService.loadUserById(TEST_ACCOUNT_ID)).thenReturn(mockUserDetails);

        // when
        ThrowingCallable action = () -> jwtService.reissue(REFRESH_TOKEN);

        // then
        assertThatExceptionOfType(DisabledAccountException.class)
                .isThrownBy(action)
                .withMessage("Disabled");
    }

    @DisplayName("jwt 재발급 - 토큰 만료")
    @Test
    void reissue_expiredToken() {
        // given
        /*  jwt payload
        {
            "sub": "1",
            "iat": 1715853021,
            "exp": 1716457821
        }
        */
        String expiredRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzE1ODUzMDIxLCJleHAiOjE3MTY0NTc4MjF9.o75SobSNxHTfXpoG_wm4vTs_iVlwRafWnTXsx9iwQ1U";
        when(jwtProvider.parseClaims(expiredRefreshToken))
                .thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));

        // when
        ThrowingCallable action = () -> jwtService.reissue(expiredRefreshToken);


        // then
        assertThatExceptionOfType(ExpiredJwtException.class)
                .isThrownBy(action)
                .withMessage("JWT expired");
    }

    @DisplayName("jwt 재발급 - 유효하지 않은 토큰")
    @ParameterizedTest
    @ValueSource(strings = {
            // 유효하지 않은 서명의 JWT
            "eyJhbGciOiJIUzI1NiIsInppcCI6IkRFRiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ0ODkyNTc4LCJleHAiOjE3NDQ4OTYxNzh9.KQje0sKwaDmD7ZHWrxbJ49Bp_KjPI8RfA2Xi9tNCGFM",
            "sdfsdfs" // 유효하지 않은 형식
    })
    void reissue_invalidToken(String invalidToken) {
        // given
        when(jwtProvider.parseClaims(invalidToken)).thenThrow(new JwtException("Invalid JWT"));

        // when
        ThrowingCallable action = () -> jwtService.reissue(invalidToken);

        // then
        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(action)
                .withMessage("Invalid JWT");
    }

    @DisplayName("jwt 재발급 - null 또는 빈 문자열 입력")
    @ParameterizedTest
    @NullAndEmptySource
    void reissue_nullOrEmpty(String invalidToken) {
        // given
        when(jwtProvider.parseClaims(invalidToken))
                .thenThrow(new IllegalArgumentException("CharSequence cannot be null or empty."));

        // when
        ThrowingCallable action = () -> jwtService.reissue(invalidToken);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(action)
                .withMessage("CharSequence cannot be null or empty.");
    }
}