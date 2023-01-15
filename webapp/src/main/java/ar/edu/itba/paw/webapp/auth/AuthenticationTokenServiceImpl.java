package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.RoleEnum;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {
    //    @Value("${authentication.jwt.validFor}")
    private Long validFor = 86400L;

//    @Value("${authentication.jwt.refreshLimit}")
    private Integer refreshLimit = 1;

    @Autowired
    private JwtTokenIssuer tokenIssuer;

    @Autowired
    private JwtTokenParser tokenParser;

    @Override
    public String issueToken(String username, Set<RoleEnum> authorities) {
        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails authenticationTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(id)
                .withUsername(username)
                .withAuthorities(authorities)
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(0)
                .withRefreshLimit(refreshLimit)
                .build();

        return tokenIssuer.issueToken(authenticationTokenDetails);
    }

    @Override
    public AuthenticationTokenDetails parseToken(String token) {
        return tokenParser.parseToken(token);
    }

    @Override
    public String refreshToken(AuthenticationTokenDetails currentTokenDetails) {
        if (!currentTokenDetails.isEligibleForRefreshment()) {
            throw new RuntimeException("This token cannot be refreshed.");
        }

        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails newTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(currentTokenDetails.getId())
                .withUsername(currentTokenDetails.getUsername())
                .withAuthorities(currentTokenDetails.getAuthorities())
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(currentTokenDetails.getRefreshCount() + 1)
                .withRefreshLimit(refreshLimit)
                .build();

        return tokenIssuer.issueToken(newTokenDetails);
    }

    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(validFor);
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}
