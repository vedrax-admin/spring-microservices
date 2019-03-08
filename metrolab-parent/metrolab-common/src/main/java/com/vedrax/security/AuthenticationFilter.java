package com.vedrax.security;

import static com.vedrax.utils.ServletUtils.getHeader;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * The authentication filter is responsible of retrieving the security token
 * (JWT) from the <code>Authorization</code> header
 *
 * @author remypenchenat
 */
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * Filter enables only for a given set of URLs.
     *
     * @param requiresAuth
     */
    public AuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = safeGetSecurityToken(request);
        //The security token will be available both in principal and credentials attributes
        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(auth);
    }

    /**
     * Retrieves the security token from the <code>Authorization</code> header
     * if any, otherwise throws {@link BadCredentialsException}
     *
     * @param request
     * @return
     */
    private String safeGetSecurityToken(HttpServletRequest request) {
        Optional<String> headerOpt = getHeader(request, SecurityConstants.AUTHORIZATION_HEADER);
        return headerOpt
                .map(header -> extractSecurityToken(header))
                .orElseThrow(() -> new BadCredentialsException("Missing Authentication token"));
    }

    private String extractSecurityToken(String header) {
        //Thrown exception if the token has only bearer or if it does not start with the prefix
        if (header.length() <= 7 || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            throw new BadCredentialsException("Authorization header is not valid");
        }
        return header.substring(7);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

}
