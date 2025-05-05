package com.demo.pteam.security.authentication;

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
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;

    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtToken rawToken = (JwtToken) authentication.getCredentials();
        JwtToken extractedToken = removeTokenPrefix(rawToken);  // prefix 제거

        try {
            String accessToken = extractedToken.accessToken();
            Claims claims = jwtService.parseClaims(accessToken);
            UserPrincipal principal = PrincipalFactory.fromClaims(claims);
            return createSuccessAuthentication(principal, extractedToken);
        } catch (ExpiredJwtException e) {   // accessToken 만료
            JwtReissueResult reissueResult = reissueToken(extractedToken.refreshToken());
            return createSuccessAuthentication(reissueResult);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Bad credentials");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException(e.getMessage(), e);
        }
    }

    private JwtReissueResult reissueToken(String refreshToken) {
        try {
            JwtUserDetails<JwtAccountInfo> userDetails = jwtService.loadUser(refreshToken);
            if (userDetails.isSuspended()) {
                throw new DisabledException("Disabled");
            }
            return jwtService.reissue(userDetails);
        } catch (ExpiredTokenException expiredTokenException) {     // refreshToken 만료
            throw new ExpiredTokenException("Expired JWT token");
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Bad credentials");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException(e.getMessage(), e);
        }
    }

    private static JwtToken removeTokenPrefix(JwtToken token) {
        if (token.isValid()) {
            return token.removePrefix();
        } else {
            throw new InvalidJwtException("Invalid JWT token");
        }
    }

    private Authentication createSuccessAuthentication(JwtReissueResult reissueResult) {
        return createSuccessAuthentication(reissueResult.principal(), reissueResult.token());
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
