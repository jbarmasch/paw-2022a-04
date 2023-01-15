package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.RoleEnum;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface AuthenticationTokenService {
    String issueToken(String username, Set<RoleEnum> authorities);

    AuthenticationTokenDetails parseToken(String token);

    String refreshToken(AuthenticationTokenDetails currentAuthenticationTokenDetails);
}
