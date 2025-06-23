package com.demo.pteam.security.authentication;

import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.authentication.dto.JwtReissueResult;
import com.demo.pteam.security.authentication.dto.JwtToken;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.exception.ExpiredTokenException;
import com.demo.pteam.security.exception.InvalidJwtException;
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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationProviderTest {
    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.M2IjaJJCfnV7Eheijp72nKtVlL1pgkghNr-Zc1i6Oks";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.NXnMg9s2NpZFIgf6EmRdGpC9qyXDOWGbRklF39vOLBg";
    private static final String AUTHORIZATION_HEADER = PREFIX + ACCESS_TOKEN;
    private static final String REFRESH_TOKEN_HEADER = PREFIX + REFRESH_TOKEN;

    private static final String EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3MjcyMDc2OTksImV4cCI6MTcyNzIxMTI5OX0.K0j4PNjduPIor-Y2aGzgwAMNIncKMrtKtc2itmg2qP0";
    private static final String EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzI3MzExOTYyLCJleHAiOjE3Mjc5MTY3NjJ9.M7C2KBVQOU1X2g6TGT1VTPRAOv3BWLYC4LB0tO-xNfA";
    private static final String EXPIRED_AUTHORIZATION_HEADER = PREFIX + EXPIRED_ACCESS_TOKEN;
    private static final String EXPIRED_REFRESH_TOKEN_HEADER = PREFIX + EXPIRED_REFRESH_TOKEN;

    @InjectMocks
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtService jwtService;

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

    private static UserPrincipal getTestPrincipal(JwtUserDetails<JwtAccountInfo> userDetails) {
        JwtAccountInfo account = userDetails.getAccount();
        boolean verified = !userDetails.isUnverified();
        return new UserPrincipal(account.id(), account.role(), verified);
    }

    @DisplayName("인증")
    @Test
    void authenticate() {
        try (MockedStatic<PrincipalFactory> factory = mockStatic(PrincipalFactory.class)) {
            // given
        /*  jwt payload
            {
                "sub": "1",
                "role": "ROLE_USER",
                "verified": true,
                "iat": 1747207699,
                "exp": 1747211299
            }
        */
            JwtToken testToken = JwtToken.ofBearer(AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
            when(authentication.getCredentials()).thenReturn(testToken);
            when(jwtService.parseClaims(ACCESS_TOKEN)).thenReturn(claims);
            UserPrincipal testPrincipal = new UserPrincipal(1L, Role.ROLE_USER, true);
            factory.when(() -> PrincipalFactory.fromClaims(claims)).thenReturn(testPrincipal);

            // when
            Authentication newAuthentication = jwtAuthenticationProvider.authenticate(authentication);

            // then
            assertThat(newAuthentication).isInstanceOf(JwtAuthenticationToken.class);
            JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) newAuthentication;
            assertThat(authenticationToken.getPrincipal()).isNotNull();
            assertThat(authenticationToken.getPrincipal()).isEqualTo(testPrincipal);
            assertThat(authenticationToken.getCredentials()).isNotNull();
            assertThat(authenticationToken.getCredentials()).isInstanceOf(JwtToken.class);
            JwtToken token = (JwtToken) authenticationToken.getCredentials();
            assertThat(token.getAuthHeader()).isEqualTo(AUTHORIZATION_HEADER);
            assertThat(token.getRefreshHeader()).isEqualTo(REFRESH_TOKEN_HEADER);
            assertThat(token.getAccessToken()).isEqualTo(ACCESS_TOKEN);
            assertThat(token.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
            assertThat(authenticationToken.getAuthorities()).isNotNull();
            assertThat(authenticationToken.getAuthorities()).hasSize(1);
            assertThat(authenticationToken.getAuthorities().iterator().next().getAuthority())
                    .isEqualTo(testPrincipal.role().name());
        }
    }

    @DisplayName("인증 - 'Bearer '로 시작하지 않는 경우")
    @Test
    void authenticate_missingBearerPrefix() {
        // given
        JwtToken invalidHeaders = JwtToken.ofBearer(ACCESS_TOKEN, REFRESH_TOKEN);
        when(authentication.getCredentials()).thenReturn(invalidHeaders);

        // when
        ThrowingCallable action = () -> jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThatExceptionOfType(InvalidJwtException.class)
                .isThrownBy(action)
                .withMessage("Invalid JWT token");
    }

    @DisplayName("인증 - 유효하지 않은 accessToken")
    @ParameterizedTest
    @ValueSource(strings = {
            // 유효하지 않은 서명의 accessToken
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsInZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDcyMDc2OTksImV4cCI6MTc0NzIxMTI5OX0.YAlicbyqBqJMSfOYs1qH0hqOjPaXC6jHIdqKeB6dON8",
            "sdfsdfs" // 유효하지 않은 형식
    })
    void authenticate_invalidAccessToken(String invalidAccessToken) {
        // given
        JwtToken invalidToken = JwtToken.ofBearer(PREFIX + invalidAccessToken, REFRESH_TOKEN_HEADER);
        when(authentication.getCredentials()).thenReturn(invalidToken);
        when(jwtService.parseClaims(invalidAccessToken)).thenThrow(new JwtException("Invalid JWT"));

        // when
        ThrowingCallable action = () -> jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThatExceptionOfType(InvalidJwtException.class)
                .isThrownBy(action);
    }

    @DisplayName("인증 - accessToken 만료로 refreshToken을 통해 토큰 재발급")
    @Test
    void authenticate_expiredAccessTokenAndValidRefreshToken() {
        // given
        JwtToken expiredToken = JwtToken.ofBearer(EXPIRED_AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        when(authentication.getCredentials()).thenReturn(expiredToken);
        when(jwtService.parseClaims(EXPIRED_ACCESS_TOKEN))
                .thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));
        JwtAccountInfo testAccountInfo = getTestAccountInfo();
        JwtUserDetailsImpl testUserDetails = getTestUserDetails(testAccountInfo);
        when(jwtService.loadUser(REFRESH_TOKEN)).thenReturn(testUserDetails);
        UserPrincipal testPrincipal = getTestPrincipal(testUserDetails);
        JwtToken reissueToken = mock(JwtToken.class);
        JwtReissueResult jwtReissueResult = new JwtReissueResult(testPrincipal, reissueToken);
        when(jwtService.reissue(testUserDetails)).thenReturn(jwtReissueResult);

        // when
        Authentication authenticate = jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThat(authenticate).isNotNull();
        assertThat(authenticate.getPrincipal()).isEqualTo(testPrincipal);
        assertThat(authenticate.getCredentials()).isNotNull();
        assertThat(authenticate.getCredentials()).isInstanceOf(JwtToken.class);
        assertThat(authenticate.getAuthorities()).hasSize(1);
    }

    @DisplayName("인증 - accessToken 만료로 refreshToken을 통해 토큰 재발급 - 조회한 사용자가 정지된 경우")
    @Test
    void authenticate_expiredAccessTokenAndValidRefreshToken_accountIsSuspended() {
        // given
        JwtToken expiredToken = JwtToken.ofBearer(EXPIRED_AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        when(authentication.getCredentials()).thenReturn(expiredToken);
        when(jwtService.parseClaims(EXPIRED_ACCESS_TOKEN))
                .thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));
        JwtAccountInfo suspendedAccount = new JwtAccountInfo(
                1L,
                Role.ROLE_USER,
                AccountStatus.SUSPENDED
        );
        JwtUserDetailsImpl testUserDetails = getTestUserDetails(suspendedAccount);
        when(jwtService.loadUser(REFRESH_TOKEN)).thenReturn(testUserDetails);

        // when
        ThrowingCallable action = () -> jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThatExceptionOfType(DisabledException.class)
                .isThrownBy(action)
                .withMessage("Disabled");
    }

    @DisplayName("인증 - accessToken, refreshToken 만료")
    @Test
    void authenticate_expiredAccessTokenAndExpiredRefreshToken() {
        // given
        JwtToken expiredToken = JwtToken.ofBearer(EXPIRED_AUTHORIZATION_HEADER, EXPIRED_REFRESH_TOKEN_HEADER);
        when(authentication.getCredentials()).thenReturn(expiredToken);
        when(jwtService.parseClaims(EXPIRED_ACCESS_TOKEN))
                .thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));
        when(jwtService.loadUser(EXPIRED_REFRESH_TOKEN))
                .thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));

        // when
        ThrowingCallable action = () -> jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThatExceptionOfType(ExpiredTokenException.class)
                .isThrownBy(action)
                .withMessage("Expired JWT token");
    }

    @DisplayName("인증 - accessToken 만료로 refreshToken을 통해 토큰 재발급 - 사용자 조회 x")
    @Test
    void authenticate_expiredAccessTokenAndValidRefreshToken_usernameNotFound() {
        // given
        JwtToken expiredToken = JwtToken.ofBearer(EXPIRED_AUTHORIZATION_HEADER, REFRESH_TOKEN_HEADER);
        when(authentication.getCredentials()).thenReturn(expiredToken);
        when(jwtService.parseClaims(EXPIRED_ACCESS_TOKEN))
                .thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));
        when(jwtService.loadUser(REFRESH_TOKEN)).thenThrow(new UsernameNotFoundException("AccountId not found: " + 1L));

        // when
        ThrowingCallable action = () -> jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(action)
                .withMessage("Bad credentials");
    }

    @DisplayName("인증 - accessToken 만료로 refreshToken을 통해 토큰 재발급 - 유효하지 않은 refreshToken")
    @ParameterizedTest
    @ValueSource(strings = {
            // 유효하지 않은 서명의 refreshToken
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjA3NzAwLCJleHAiOjE3NDc4MTI1MDB9.eR2aFv_159TMilurSHSIV-3yuvE1Dpv5VbEVVSNC1NI",
            "sdfsdfs" // 유효하지 않은 형식
    })
    void authenticate_expiredAccessTokenAndInvalidRefreshToken(String invalidRefreshToken) {
        // given
        JwtToken expiredToken = JwtToken.ofBearer(EXPIRED_AUTHORIZATION_HEADER, PREFIX + invalidRefreshToken);
        when(authentication.getCredentials()).thenReturn(expiredToken);
        when(jwtService.parseClaims(EXPIRED_ACCESS_TOKEN))
                .thenThrow(new ExpiredJwtException(mock(Header.class), claims, "JWT expired"));
        when(jwtService.loadUser(invalidRefreshToken)).thenThrow(new JwtException("Invalid JWT"));

        // when
        ThrowingCallable action = () -> jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThatExceptionOfType(InvalidJwtException.class)
                .isThrownBy(action);
    }


    @DisplayName("지원 여부 확인 - true")
    @Test
    void supports_true() {
        // given
        Class<JwtAuthenticationToken> authenticationClass = JwtAuthenticationToken.class;

        // when
        boolean supports = jwtAuthenticationProvider.supports(authenticationClass);

        // then
        assertThat(supports).isTrue();
    }

    @DisplayName("지원 여부 확인 - false")
    @Test
    void supports_false() {
        // given
        Class<UsernamePasswordAuthenticationToken> authenticationClass = UsernamePasswordAuthenticationToken.class;

        // when
        boolean supports = jwtAuthenticationProvider.supports(authenticationClass);

        // then
        assertThat(supports).isFalse();
    }
}