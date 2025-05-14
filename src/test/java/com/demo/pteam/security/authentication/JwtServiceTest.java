package com.demo.pteam.security.authentication;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.authentication.dto.JwtReissueResult;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.jwt.JwtProvider;
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
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ1ODUzMDIxLCJleHAiOjE3NDY0NTc4MjF9._TgcbT4TByg2DChjojzasus9Ece6LVHr9PECZNU6YI0";

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

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

    @DisplayName("사용자 조회")
    @Test
    void loadUser() {
        // given
        String testRefreshToken = REFRESH_TOKEN;
        Long testAccountId = 1L;
        when(jwtProvider.parseClaims(testRefreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(testAccountId.toString());

        JwtAccountInfo testAccountInfo = getTestAccountInfo();
        JwtUserDetailsImpl testUserDetails = getTestUserDetails(testAccountInfo);
        when(jwtUserDetailsService.loadUserById(testAccountId)).thenReturn(testUserDetails);

        // when
        JwtUserDetails<JwtAccountInfo> userDetails = jwtService.loadUser(testRefreshToken);

        // then
        assertThat(userDetails).isInstanceOf(JwtUserDetailsImpl.class);
        assertThat(userDetails.getAccount()).isEqualTo(testAccountInfo);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo(testAccountInfo.role().name());
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isDeleted()).isFalse();
        assertThat(userDetails.isUnverified()).isFalse();
        assertThat(userDetails.isSuspended()).isFalse();
    }

    @DisplayName("사용자 조회 - 조회된 계정이 없는 경우")
    @Test
    void loadUser_unregisteredUsername() {
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
        when(jwtUserDetailsService.loadUserById(unregisteredAccountId))
                .thenThrow(new UsernameNotFoundException("AccountId not found: " + unregisteredAccountId));

        // when
        ThrowingCallable action = () -> jwtService.loadUser(testRefreshToken);

        // then
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(action)
                .withMessage("AccountId not found: " + unregisteredAccountId);
    }

    @DisplayName("사용자 조회 - 토큰 만료")
    @Test
    void loadUser_expiredToken() {
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
        ThrowingCallable action = () -> jwtService.loadUser(expiredRefreshToken);


        // then
        assertThatExceptionOfType(ExpiredJwtException.class)
                .isThrownBy(action)
                .withMessage("JWT expired");
    }

    @DisplayName("사용자 조회 - 유효하지 않은 토큰")
    @ParameterizedTest
    @CsvSource({
            // 유효하지 않은 서명의 JWT
            "eyJhbGciOiJIUzI1NiIsInppcCI6IkRFRiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ0ODkyNTc4LCJleHAiOjE3NDQ4OTYxNzh9.KQje0sKwaDmD7ZHWrxbJ49Bp_KjPI8RfA2Xi9tNCGFM",
            "sdfsdfs", // 유효하지 않은 형식
    })
    void loadUser_invalidToken(String invalidToken) {
        // given
        when(jwtProvider.parseClaims(invalidToken)).thenThrow(new JwtException("Invalid JWT"));

        // when
        ThrowingCallable action = () -> jwtProvider.parseClaims(invalidToken);

        // then
        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(action)
                .withMessage("Invalid JWT");
    }

    @DisplayName("사용자 조회 - null 또는 빈 문자열 입력")
    @ParameterizedTest
    @CsvSource({
            "'',''",    // 빈 문자열
            ","  // null
    })
    void loadUser_nullOrEmpty(String invalidToken) {
        // given
        when(jwtProvider.parseClaims(invalidToken))
                .thenThrow(new IllegalArgumentException("CharSequence cannot be null or empty."));

        // when
        ThrowingCallable action = () -> jwtProvider.parseClaims(invalidToken);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(action)
                .withMessage("CharSequence cannot be null or empty.");
    }


    @DisplayName("jwt 재발급")
    @Test
    void reissue() {
        try(MockedStatic<PrincipalFactory> factory = mockStatic(PrincipalFactory.class)) {
            // given
            JwtAccountInfo testAccountInfo = getTestAccountInfo();
            JwtUserDetailsImpl testUserDetails = getTestUserDetails(testAccountInfo);
            UserPrincipal expectedPrincipal = new UserPrincipal(
                    testUserDetails.getAccount().id(),
                    testUserDetails.getAccount().role(),
                    testUserDetails.isUnverified()
            );
            factory.when(() -> PrincipalFactory.fromUser(testUserDetails))
                    .thenReturn(expectedPrincipal);

            String testAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDU4NTMwMjEsImV4cCI6MTc0NjQ1NzgyMX0.6mOSoVzZ63-SZjrl_gp9yh_dmoDNIvXDsN9X0M54wA8";
            String testRefreshToken = REFRESH_TOKEN;
            when(jwtProvider.generateAccessToken(expectedPrincipal)).thenReturn(testAccessToken);
            when(jwtProvider.generateRefreshToken(expectedPrincipal)).thenReturn(testRefreshToken);

            // when
            JwtReissueResult reissue = jwtService.reissue(testUserDetails);

            // then
            assertThat(reissue).isNotNull();
            assertThat(reissue.principal()).isNotNull();
            assertThat(reissue.principal()).isEqualTo(expectedPrincipal);
            assertThat(reissue.token()).isNotNull();
            assertThat(reissue.token().getAccessToken()).isEqualTo(testAccessToken);
            assertThat(reissue.token().getRefreshToken()).isEqualTo(testRefreshToken);
        }
    }
}