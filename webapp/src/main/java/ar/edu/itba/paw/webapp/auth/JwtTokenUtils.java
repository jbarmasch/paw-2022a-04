package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.RoleEnum;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.SignatureException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
//    @Value("${authentication.jwt.secret}")
    private String secret = "hola";

    //    @Value("${authentication.jwt.clockSkew}")
    private Long clockSkew = 10L;

    //    @Value("${authentication.jwt.audience}")
    private String audience = "http://181.46.186.8:2555";

    //    @Value("${authentication.jwt.issuer}")
    private String issuer = "http://181.46.186.8:2555";

    //    @Value("${authentication.jwt.claimNames.authorities}")
    private String authoritiesClaimName = "authorities";

    private String isRefreshName = "isRefresh";

    public String issueToken(AuthenticationTokenDetails authenticationTokenDetails) {
        return Jwts.builder()
                .setId(authenticationTokenDetails.getId())
                .setIssuer(issuer)
                .setAudience(audience)
                .setSubject(authenticationTokenDetails.getUsername())
                .setIssuedAt(Date.from(authenticationTokenDetails.getIssuedDate().toInstant()))
                .setExpiration(Date.from(authenticationTokenDetails.getExpirationDate().toInstant()))
                .claim(authoritiesClaimName, authenticationTokenDetails.getAuthorities())
                .claim(isRefreshName, authenticationTokenDetails.isRefresh())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public AuthenticationTokenDetails parseToken(String token) {
//        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .requireAudience(audience)
                    .setAllowedClockSkewSeconds(clockSkew)
                    .parseClaimsJws(token)
                    .getBody();

            return new AuthenticationTokenDetails.Builder()
                    .withId(extractTokenIdFromClaims(claims))
                    .withUsername(extractUsernameFromClaims(claims))
                    .withAuthorities(extractAuthoritiesFromClaims(claims))
                    .withIssuedDate(extractIssuedDateFromClaims(claims))
                    .withExpirationDate(extractExpirationDateFromClaims(claims))
                    .withIsRefresh(extractIsRefreshFromClaims(claims))
                    .build();

//        } catch (ExpiredJwtException e) {
//            throw new RuntimeException("Expired token", e);
//        } catch (InvalidClaimException e) {
//            throw new RuntimeException("Invalid value for claim \"" + e.getClaimName() + "\"", e);
//        } catch (Exception e) {
//            throw new RuntimeException("Invalid token", e);
//        }
    }

    private String extractTokenIdFromClaims(@NotNull Claims claims) {
        return (String) claims.get(Claims.ID);
    }

    private String extractUsernameFromClaims(@NotNull Claims claims) {
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    private Set<RoleEnum> extractAuthoritiesFromClaims(@NotNull Claims claims) {
        List<String> rolesAsString = (List<String>) claims.getOrDefault(authoritiesClaimName, new ArrayList<>());
        return rolesAsString.stream().map(RoleEnum::valueOf).collect(Collectors.toSet());
    }

    private ZonedDateTime extractIssuedDateFromClaims(@NotNull Claims claims) {
        return ZonedDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault());
    }

    private ZonedDateTime extractExpirationDateFromClaims(@NotNull Claims claims) {
        return ZonedDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    private boolean extractIsRefreshFromClaims(@NotNull Claims claims) {
        return (boolean) claims.get(isRefreshName);
    }
}

