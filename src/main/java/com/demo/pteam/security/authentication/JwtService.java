package com.demo.pteam.security.authentication;

import com.demo.pteam.security.authentication.dto.JwtReissueResult;
import com.demo.pteam.security.authentication.dto.JwtToken;
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

    public JwtUserDetails<JwtAccountInfo> loadUser(String refreshToken) throws IllegalArgumentException, JwtException, UsernameNotFoundException {
        Claims claims = parseClaims(refreshToken);
        Long id = Long.valueOf(claims.getSubject());
        return jwtUserDetailsService.loadUserById(id);
    }

    public Claims parseClaims(String token) throws IllegalArgumentException, JwtException {
        return jwtProvider.parseClaims(token);
    }

    public JwtReissueResult reissue(JwtUserDetails<JwtAccountInfo> userDetails) {
        UserPrincipal principal = PrincipalFactory.fromUser(userDetails);
        return new JwtReissueResult(principal, createJwtToken(principal));
    }

    private JwtToken createJwtToken(UserPrincipal principal) {
        String reissueAccessToken = jwtProvider.generateAccessToken(principal);
        String reissueRefreshToken = jwtProvider.generateRefreshToken(principal);
        return new JwtToken(reissueAccessToken, reissueRefreshToken);
    }
}
