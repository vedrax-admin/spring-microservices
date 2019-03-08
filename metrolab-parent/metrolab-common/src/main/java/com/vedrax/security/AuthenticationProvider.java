package com.vedrax.security;

import java.util.Optional;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *
 * @author remypenchenat
 */
@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final TokenService tokenService;

    @Autowired
    public AuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails ud, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    /**
     * Retrieve authenticated user with the provided security token
     *
     * @param string
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String string, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String token = getSecurityToken(authentication);
        return tokenService.parseToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("JWT Token [" + token + "] is not valid"));
    }

    /**
     * Extracts security token from {@link UsernamePasswordAuthenticationToken}
     *
     * @param authentication
     * @return
     */
    private String getSecurityToken(UsernamePasswordAuthenticationToken authentication) {
        Validate.notNull(authentication, "A UsernamePasswordAuthenticationToken must be provided");

        Object token = authentication.getCredentials();
        return Optional
                .ofNullable(token)
                .map(String::valueOf)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find security token for [" + token + "]."));
    }

}
