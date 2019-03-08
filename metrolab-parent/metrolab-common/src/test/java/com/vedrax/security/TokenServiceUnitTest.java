package com.vedrax.security;

import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author remypenchenat
 */
public class TokenServiceUnitTest {

    private TokenService tokenService;
    private UserPrincipal user;

    @Before
    public void setUp() {
        tokenService = new TokenServiceImpl();
        user = new UserPrincipal("finance@vedrax.com", "Remy Penchenat", "ADMIN");
    }

    @Test
    public void whenExpiringHasPrincipalAsArgument_thenReturnsJWT() {
        String jwt = generateJWT();
        assertThat(jwt, notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void whenExpiringHasNoPrincipalAsArgument_thenThrowsException() {
        tokenService.expiring(null);
    }

    @Test
    public void whenParseToken_thenReturnsPrincipal() {
        String jwt = generateJWT();
        Optional<UserPrincipal> authenticatedUser = tokenService.parseToken(jwt);
        validate(authenticatedUser.get());
    }

    @Test
    public void whenParseInvalidToken_thenReturnsNoPrincipal() {
        Optional<UserPrincipal> authenticatedUser = tokenService.parseToken("invalid");
        assertThat(authenticatedUser.isPresent(), is(false));
    }

    private String generateJWT() {
        return tokenService.expiring(user);
    }

    private void validate(UserPrincipal authenticatedUser) {
        assertThat(authenticatedUser, notNullValue());
        assertThat(authenticatedUser.getFullName(), is(user.getFullName()));
        assertThat(authenticatedUser.getUsername(), is(user.getUsername()));
        assertThat(authenticatedUser.getRole(), is(user.getRole()));
    }

}
