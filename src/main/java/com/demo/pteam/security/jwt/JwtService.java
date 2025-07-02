package com.demo.pteam.security.jwt;

import com.demo.pteam.security.authentication.JwtUserDetails;
import com.demo.pteam.security.authentication.JwtUserDetailsService;
import com.demo.pteam.security.authentication.dto.JwtReissueResult;
import com.demo.pteam.security.authentication.dto.JwtToken;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.jwt.exception.DisabledAccountException;
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
    private final TokenBlackList tokenBlackList;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTTL;

    public JwtService(JwtProvider jwtProvider, JwtUserDetailsService jwtUserDetailsService, TokenStore tokenStore, TokenBlackList tokenBlackList) {
        this.jwtProvider = jwtProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenStore = tokenStore;
        this.tokenBlackList = tokenBlackList;
    }

    private JwtUserDetails<JwtAccountInfo> loadUser(String refreshToken) throws IllegalArgumentException, JwtException, UsernameNotFoundException {
        Long id = extractSubject(refreshToken);
        validateToken(refreshToken, id);
        return jwtUserDetailsService.loadUserById(id);
    }

    public void invalidateRefreshToken(String refreshToken, String reason) throws IllegalArgumentException, JwtException {
        String rawRefreshToken = removeBearerPrefix(refreshToken);
        Long id = extractSubject(rawRefreshToken);
        validateToken(rawRefreshToken, id);
        saveBlackList(id, rawRefreshToken, reason);
    }

    private String removeBearerPrefix(String token) throws IllegalArgumentException {
        String prefix = "Bearer ";
        if (token.startsWith(prefix)) {
            return token.substring(prefix.length());
        } else {
            throw new IllegalArgumentException("Invalid header");
        }
    }

    private Long extractSubject(String token) throws IllegalArgumentException, JwtException {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    private void validateToken(String refreshToken, Long id) throws JwtException {
        if (tokenStore.isInvalid(id, refreshToken) || tokenBlackList.isBlackListed(refreshToken)) {
            throw new JwtException("Invalid JWT");
        }
    }

    public Claims parseClaims(String token) throws IllegalArgumentException, JwtException {
        return jwtProvider.parseClaims(token);
    }

    public JwtReissueResult reissue(String refreshToken) throws DisabledAccountException, IllegalArgumentException, JwtException, UsernameNotFoundException {
        JwtUserDetails<JwtAccountInfo> userDetails = loadUser(refreshToken);
        if (userDetails.isSuspended()) {
            throw new DisabledAccountException("Disabled");
        }
        UserPrincipal principal = PrincipalFactory.fromUser(userDetails);
        saveBlackList(principal.id(), refreshToken, "reissue");
        JwtToken jwtToken = createJwtToken(principal);
        return new JwtReissueResult(principal, jwtToken);
    }

    private void saveBlackList(Long id, String refreshToken, String reason) throws JwtException {
        TokenData tokenData = tokenStore.findByAccountId(id).orElseThrow(() -> new JwtException("Invalid JWT"));
        tokenStore.delete(id);
        tokenBlackList.save(refreshToken, reason, tokenData.expirationMillis());
    }

    public JwtToken createJwtToken(UserPrincipal principal) {
        Date now = new Date();
        String accessToken = jwtProvider.generateAccessToken(principal, now);
        String refreshToken = jwtProvider.generateRefreshToken(principal, now);
        tokenStore.save(principal.id(), refreshToken, now.getTime() + refreshTokenTTL);
        return JwtToken.ofRaw(accessToken, refreshToken);
    }
}
