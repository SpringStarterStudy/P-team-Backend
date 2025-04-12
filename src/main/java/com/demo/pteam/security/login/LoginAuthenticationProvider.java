package com.demo.pteam.security.login;

import com.demo.pteam.security.principal.CustomUserDetails;
import com.demo.pteam.security.principal.UserPrincipal;
import com.demo.pteam.security.principal.PrincipalFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginAuthenticationProvider extends DaoAuthenticationProvider {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{4,12}$";
    private static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        validateUsernameFormat(authentication.getName(), authentication.getCredentials().toString());
        return super.authenticate(authentication);
    }

    private void validateUsernameFormat(String username, String password) {
        if (!username.matches(USERNAME_PATTERN) || !password.matches(PASSWORD_PATTERN)) {
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        if (principal instanceof CustomUserDetails userDetails) {
            UserPrincipal userPrincipal = PrincipalFactory.fromUser(userDetails);
            return super.createSuccessAuthentication(userPrincipal, authentication, user);
        } else {
            throw new InternalAuthenticationServiceException("Unexpected principal type");
        }
    }
}
