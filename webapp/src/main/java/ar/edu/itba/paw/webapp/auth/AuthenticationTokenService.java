package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Component
public class AuthenticationTokenService {
    //    @Value("${authentication.jwt.validFor}")
    private Long validFor = 86400L;

    @Autowired
    private JwtTokenUtils tokenUtils;

    public String issueAccessToken(String username, Set<RoleEnum> authorities) {
        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = issuedDate.plusSeconds(validFor);

        AuthenticationTokenDetails authenticationTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(id)
                .withUsername(username)
                .withAuthorities(authorities)
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .build();

        return tokenUtils.issueToken(authenticationTokenDetails);
    }

    public AuthenticationTokenDetails parseToken(String token) {
        return tokenUtils.parseToken(token);
    }

    public String issueRefreshToken(String username, Set<RoleEnum> authorities) {
        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = issuedDate.plusSeconds(validFor * 7);

        AuthenticationTokenDetails authenticationTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(id)
                .withUsername(username)
                .withAuthorities(authorities)
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withIsRefresh(true)
                .build();

        return tokenUtils.issueToken(authenticationTokenDetails);
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}
