package com.vedrax.security;

/**
 *
 * @author remypenchenat
 */
class SecurityConstants {

    public static final String ISSUER = "vedrax.com";
    public static final String JWT_SECRET = "secret-key";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";

}
