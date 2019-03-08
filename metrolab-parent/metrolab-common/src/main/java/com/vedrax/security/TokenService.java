package com.vedrax.security;

import java.util.Optional;

/**
 * 
 * Interface for JWT management
 *
 * @author remypenchenat
 */
public interface TokenService {

    /**
     * Generate expiring token
     *
     * @param user the user for which the token will be generated
     * @return the JWT token
     */
    public String expiring(UserPrincipal user);

    /**
     * Parse specified String as a JWT token.
     *
     * @param token the JWT token to parse
     * @return the LoggedUser object extracted from specified token or null if a
     * token is invalid.
     */
    public Optional<UserPrincipal> parseToken(String token);

}
