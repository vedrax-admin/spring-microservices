package com.vedrax.security;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author remypenchenat
 */
public class AuthenticationProviderUnitTest {

    private AuthenticationProvider authenticationProvider;
    private UsernamePasswordAuthenticationToken validAuthenticationToken;
    private UsernamePasswordAuthenticationToken invalidAuthenticationToken;
    private UsernamePasswordAuthenticationToken emptyAuthenticationToken;
    private final String VALID_SECURITY_TOKEN = "securityToken";
    private final String INVALID_SECURITY_TOKEN = "invalid";
    private TokenService tokenService;
    private UserPrincipal userPrincipal;

    @Before
    public void setUp() {
        tokenService = mock(TokenService.class);
        authenticationProvider = new AuthenticationProvider(tokenService);
        validAuthenticationToken = new UsernamePasswordAuthenticationToken(VALID_SECURITY_TOKEN, VALID_SECURITY_TOKEN);
        invalidAuthenticationToken = new UsernamePasswordAuthenticationToken(INVALID_SECURITY_TOKEN, INVALID_SECURITY_TOKEN);
        emptyAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null);
        userPrincipal = new UserPrincipal("finance@vedrax.com", "Remy Penchenat", "ADMIN");
        when(tokenService.parseToken(VALID_SECURITY_TOKEN)).thenReturn(Optional.of(userPrincipal));
        when(tokenService.parseToken(INVALID_SECURITY_TOKEN)).thenReturn(Optional.empty());
    }

    @Test
    public void whenRetrieveUserHasValidAuthenticationToken_thenReturnsPrincipal() {
        UserDetails result = authenticationProvider.retrieveUser("", validAuthenticationToken);

        checkUserPrincipal(result);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void whenRetrieveUserHasInvalidAuthenticationToken_thenThrowsException() {
        authenticationProvider.retrieveUser("", invalidAuthenticationToken);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void whenRetrieveUserHasEmptyAuthenticationToken_thenThrowsException() {
        authenticationProvider.retrieveUser("", emptyAuthenticationToken);
    }

    @Test(expected = NullPointerException.class)
    public void whenRetrieveUserHasNoAuthenticationToken_thenThrowsException() {
        authenticationProvider.retrieveUser("", null);
    }

    private void checkUserPrincipal(UserDetails result) {

        UserPrincipal returnedUserPrincipal = (UserPrincipal) result;

        assertEquals(userPrincipal.getUsername(), returnedUserPrincipal.getUsername());
        assertEquals(userPrincipal.getFullName(), returnedUserPrincipal.getFullName());
        assertEquals(userPrincipal.getRole(), returnedUserPrincipal.getRole());
    }

}
