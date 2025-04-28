package com.demo.pteam.security.authentication;

import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.exception.ExpiredTokenException;
import com.demo.pteam.security.exception.InvalidJwtException;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.principal.PrincipalFactory;
import com.demo.pteam.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtProvider jwtProvider;
    private JwtUserDetailsService jwtUserDetailsService;

    public JwtAuthenticationProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public void setJwtUserDetailsService(JwtUserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtToken rawToken = (JwtToken) authentication.getCredentials();
        JwtToken extractedToken = removeTokenPrefix(rawToken);  // prefix 제거

        String accessToken = extractedToken.accessToken();
        Claims claims = parseClaimsSafely(accessToken);
        if (jwtProvider.isExpired(claims)) {    // accessToken 만료
            String refreshToken = extractedToken.refreshToken();
            return reissueToken(refreshToken);
        }
        try {
            UserPrincipal principal = PrincipalFactory.fromClaims(claims);
            return createSuccessAuthentication(principal, extractedToken);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidJwtException("JWT Claims parsing failed.", e);
        }
    }

    private static JwtToken removeTokenPrefix(JwtToken token) {
        String accessToken = token.accessToken();
        String refreshToken = token.refreshToken();

        if (accessToken.startsWith("Bearer ") && refreshToken.startsWith("Bearer ") ) {
            String extractedAccessToken = accessToken.substring(7);
            String extractedRefreshToken = refreshToken.substring(7);
            return new JwtToken(extractedAccessToken, extractedRefreshToken);
        } else {
            throw new InvalidJwtException("Invalid JWT token");
        }
    }

    private Claims parseClaimsSafely(String token) {
        try {
            return jwtProvider.parseClaims(token);
        } catch (JwtException e) {      // jwt 서명 불일치
            throw new InvalidJwtException(e.getMessage(), e);
        }
    }

    private Authentication reissueToken(String refreshToken) throws AuthenticationException {
        Claims claims = parseClaimsSafely(refreshToken);
        if (jwtProvider.isExpired(claims)) {    // refreshToken 만료
            throw new ExpiredTokenException("Expired JWT token");
        }
        try {
            Long id = Long.valueOf(claims.getSubject());
            JwtUserDetails<JwtAccountInfo> jwtUserDetails = jwtUserDetailsService.loadUserById(id); // TODO: 계정 상태 체크 후 예외처리 필요
            UserPrincipal principal = PrincipalFactory.fromUser(jwtUserDetails);
            JwtToken reissueToken = createJwtToken(principal);
            return createSuccessAuthentication(principal, reissueToken);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Bad credentials");
        } catch (NullPointerException | NumberFormatException e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    private JwtToken createJwtToken(UserPrincipal principal) {
        String reissueAccessToken = jwtProvider.generateAccessToken(principal);
        String reissueRefreshToken = jwtProvider.generateRefreshToken(principal);
        return new JwtToken(reissueAccessToken, reissueRefreshToken);
    }

    private Authentication createSuccessAuthentication(UserPrincipal principal, JwtToken token) {
        return JwtAuthenticationToken.authenticated(principal, token, List.of(createGrantedAuthority(principal.role())));
    }

    private GrantedAuthority createGrantedAuthority(Role role) {
        return new SimpleGrantedAuthority(role.name());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
