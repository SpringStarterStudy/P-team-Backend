package com.demo.pteam.security.login;

import com.demo.pteam.security.principal.UserPrincipal;
import com.demo.pteam.security.principal.PrincipalFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class LoginAuthenticationProvider extends DaoAuthenticationProvider {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{4,12}$";
    private static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}";

    public LoginAuthenticationProvider() {
        setPreAuthenticationChecks((user) -> {});
        setPostAuthenticationChecks(new DefaultPostAuthenticationChecks());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        validateLoginPattern(authentication.getName(), authentication.getCredentials().toString());
        return super.authenticate(authentication);
    }

    private void validateLoginPattern(String username, String password) {
        if (!username.matches(USERNAME_PATTERN) || !password.matches(PASSWORD_PATTERN)) {
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        if (principal instanceof LoginUserDetails userDetails) {
            UserPrincipal userPrincipal = PrincipalFactory.fromUser(userDetails);
            return super.createSuccessAuthentication(userPrincipal, authentication, user);
        } else {
            throw new InternalAuthenticationServiceException("Unexpected principal type");
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        private DefaultPostAuthenticationChecks() {
        }

        public void check(UserDetails user) {
            if (!user.isEnabled()) {
                LoginAuthenticationProvider.super.logger.debug("Failed to authenticate since user account is disabled");
                throw new DisabledException(LoginAuthenticationProvider.super.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "Account is disabled"));
            }
        }
    }
}
