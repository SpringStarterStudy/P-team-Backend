package com.demo.pteam.security.authentication;

import com.demo.pteam.security.authentication.dto.JwtReissueResult;
import com.demo.pteam.security.authentication.dto.JwtToken;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.jwt.TokenStore;
import com.demo.pteam.security.principal.PrincipalFactory;
import com.demo.pteam.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final TokenStore tokenStore;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTTL;

    public JwtService(JwtProvider jwtProvider, JwtUserDetailsService jwtUserDetailsService, TokenStore tokenStore) {
        this.jwtProvider = jwtProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenStore = tokenStore;
    }

    public JwtUserDetails<JwtAccountInfo> loadUser(String refreshToken) throws IllegalArgumentException, JwtException, UsernameNotFoundException {
        Claims claims = parseClaims(refreshToken);
        Long id = Long.valueOf(claims.getSubject());
        if (tokenStore.isInvalid(id, refreshToken)) {
            throw new JwtException("Invalid JWT");
        }
        return jwtUserDetailsService.loadUserById(id);
    }

    public Claims parseClaims(String token) throws IllegalArgumentException, JwtException {
        return jwtProvider.parseClaims(token);
    }

    public JwtReissueResult reissue(JwtUserDetails<JwtAccountInfo> userDetails) {
        UserPrincipal principal = PrincipalFactory.fromUser(userDetails);
        JwtToken jwtToken = createJwtToken(principal);
        return new JwtReissueResult(principal, jwtToken);
    }

    public JwtToken createJwtToken(UserPrincipal principal) {
        Date now = new Date();
        String accessToken = jwtProvider.generateAccessToken(principal, now);
        String refreshToken = jwtProvider.generateRefreshToken(principal, now);
        tokenStore.save(principal.id(), refreshToken, now.getTime() + refreshTokenTTL);
        return JwtToken.ofRaw(accessToken, refreshToken);
    }
}
