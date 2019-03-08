package com.vedrax.security;

import static com.vedrax.security.SecurityConstants.ISSUER;
import static com.vedrax.security.SecurityConstants.JWT_SECRET;
import static com.vedrax.utils.DateUtils.addUnitToLocalDateTime;
import static com.vedrax.utils.DateUtils.convertToDateTime;
import static com.vedrax.utils.DateUtils.convertToLocalDateTime;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

/**
 *
 * Service for creating security token (JWT)
 *
 * @author remypenchenat
 */
@Service
public class TokenServiceImpl implements TokenService {

    /**
     * Parse the provided JWT
     *
     * @param token the JWT to be parsed
     * @return an optional {@link UserPrincipal}
     */
    @Override
    public Optional<UserPrincipal> parseToken(String token) {
        try {
            Claims body = getClaimsWithToken(token);
            UserPrincipal userPrincipal = claimsToPrincipal(body);
            return Optional.of(userPrincipal);
        } catch (JwtException | ClassCastException e) {
            return Optional.empty();
        }
    }

    /**
     * Get {@link Claims} with the provided JWT
     *
     * @param token
     * @return
     */
    private Claims getClaimsWithToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .requireIssuer(ISSUER)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get {@link UserPrincipal} with the provided {@link Claims}
     *
     * @param body
     * @return
     */
    private UserPrincipal claimsToPrincipal(Claims body) {
        String username = body.getSubject();
        String fullName = (String) body.get(UserPrincipal.FULL_NAME);
        String role = (String) body.get(UserPrincipal.ROLE);

        return new UserPrincipal(username, fullName, role);
    }

    /**
     * Creates an expiring JWT - 24 hours
     *
     * @param user the {@link UserPrincipal}
     * @return
     */
    @Override
    public String expiring(UserPrincipal user) {
        return createToken(user, 86400);//24hours
    }

    /**
     * Creates an expiring JWT
     *
     * @param user the {@link UserPrincipal}
     * @param expiresInSec the expiration in seconds
     * @return
     */
    private String createToken(final UserPrincipal user, final int expiresInSec) {
        Validate.notNull(user, "user should not be null");

        Claims claims = setClaims();
        setDuration(claims, expiresInSec);
        setAttributes(claims, user);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    /**
     * Set a new @{link Claims}
     *
     * @return
     */
    private Claims setClaims() {
        LocalDateTime now = LocalDateTime.now();
        return Jwts
                .claims()
                .setIssuer(ISSUER)
                .setIssuedAt(convertToDateTime(now));
    }

    /**
     * Set the expiration time
     *
     * @param claims the @{link Claims}
     * @param expiresInSec the expiration in seconds
     */
    private void setDuration(Claims claims, int expiresInSec) {
        if (expiresInSec > 0) {
            LocalDateTime issuedAt = convertToLocalDateTime(claims.getIssuedAt());
            LocalDateTime expiresAt = addUnitToLocalDateTime(issuedAt, expiresInSec, ChronoUnit.SECONDS);
            claims.setExpiration(convertToDateTime(expiresAt));
        }
    }

    /**
     * Set the attributes of the provided {@link Claims} with the
     * {@link UserPrincipal}
     *
     * @param claims the @{link Claims}
     * @param user the {@link UserPrincipal}
     */
    private void setAttributes(Claims claims, UserPrincipal user) {
        claims.setSubject(user.getUsername());
        claims.put(UserPrincipal.FULL_NAME, user.getFullName());
        claims.put(UserPrincipal.ROLE, user.getRole());
    }

}
