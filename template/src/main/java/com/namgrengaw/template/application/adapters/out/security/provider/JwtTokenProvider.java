package com.namgrengaw.template.application.adapters.out.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.namgrengaw.template.application.core.domain.security.Token;
import com.namgrengaw.template.application.config.security.JwtSecurityProperties;
import com.namgrengaw.template.application.exceptions.InvalidJwtAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    public static final String ACCESS_TOKEN = "access";
    public static final String REFRESH_TOKEN = "refresh";
    public static final String BEARER = "Bearer ";
    public static final String TYPE = "type";
    public static final String ROLES = "roles";
    public static final String AUTHORIZATION = "Authorization";

    private final JwtSecurityProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(JwtSecurityProperties jwtSecurityProperties, UserDetailsService userDetailsService) {
        this.jwtProperties = jwtSecurityProperties;
        this.userDetailsService = userDetailsService;
    }

    public Token createAccessToken(String username, List<String> roles) {
        final Date now = new Date();
        final Date expiresAt = new Date(now.getTime() + jwtProperties.getValidityInMilliseconds());
        final Date refreshTokenExpiration = new Date(now.getTime() + jwtProperties.getValidityInMilliseconds() * 3);

        final String accessToken = buildToken(username, roles, now, expiresAt, ACCESS_TOKEN);
        final String refreshToken = buildToken(username, roles, now, refreshTokenExpiration, REFRESH_TOKEN);
        return new Token(username, true, now, expiresAt, accessToken, refreshToken);
    }

    public Token refreshToken(String refreshToken) {
        if(!tokenContainsBearer(refreshToken)) throw new InvalidJwtAuthenticationException("Invalid JWT refresh token. Missing Bearer prefix.");
        final String token = refreshToken.substring(BEARER.length());

        final DecodedJWT decodedJWT = verifyToken(token);

        if (!REFRESH_TOKEN.equals(decodedJWT.getClaim(TYPE).asString()))
            throw new InvalidJwtAuthenticationException("Invalid refresh token");

        return createAccessToken(
                decodedJWT.getSubject(),
                decodedJWT.getClaim(ROLES).asList(String.class)
        );
    }

    public Authentication getAuthentication(String token) {
        final DecodedJWT decodedJWT = verifyToken(token);
        final UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            verifyToken(token);
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        return StringUtils.startsWith(bearer, BEARER)
                ? bearer.substring(7)
                : null;
    }

    private DecodedJWT verifyToken(String token) {
        if (StringUtils.isBlank(token)) throw new InvalidJwtAuthenticationException("JWT token is missing");
        final JWTVerifier verifier = JWT.require(jwtProperties.getAlgorithm()).build();

        return verifier.verify(token);
    }

    private static boolean tokenContainsBearer(String token) {
        return StringUtils.isNotBlank(token) && token.startsWith(BEARER);
    }

    private String buildToken(
            String username,
            List<String> roles,
            Date issuedAt,
            Date expiresAt,
            String type
    ) {
        final String issuer = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withSubject(username)
                .withClaim(ROLES, roles)
                .withClaim(TYPE, type)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(jwtProperties.getAlgorithm());
    }


}
