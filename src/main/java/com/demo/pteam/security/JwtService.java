package com.demo.pteam.security;

import com.demo.pteam.security.authentication.dto.JwtReissueResult;
import com.demo.pteam.security.authentication.dto.JwtToken;
import com.demo.pteam.security.authentication.JwtUserDetails;
import com.demo.pteam.security.authentication.JwtUserDetailsService;
import com.demo.pteam.security.dto.JwtAccountInfo;
import com.demo.pteam.security.jwt.JwtProvider;
import com.demo.pteam.security.principal.PrincipalFactory;
import com.demo.pteam.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtService(JwtProvider jwtProvider, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    public JwtReissueResult reissue(String refreshToken) throws IllegalArgumentException, JwtException, UsernameNotFoundException {
        Claims claims = parseClaims(refreshToken);
        Long id = Long.valueOf(claims.getSubject());
        JwtUserDetails<JwtAccountInfo> jwtUserDetails = jwtUserDetailsService.loadUserById(id); // TODO: 계정 상태 체크 후 예외처리 필요
        UserPrincipal principal = PrincipalFactory.fromUser(jwtUserDetails);
        return new JwtReissueResult(principal, createJwtToken(principal));
    }

    public Claims parseClaims(String token) throws IllegalArgumentException, JwtException {
        return jwtProvider.parseClaims(token);
    }

    private JwtToken createJwtToken(UserPrincipal principal) {
        String reissueAccessToken = jwtProvider.generateAccessToken(principal);
        String reissueRefreshToken = jwtProvider.generateRefreshToken(principal);
        return new JwtToken(reissueAccessToken, reissueRefreshToken);
    }
}
