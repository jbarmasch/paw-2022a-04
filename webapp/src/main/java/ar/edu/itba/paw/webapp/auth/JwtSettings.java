package ar.edu.itba.paw.webapp.auth;

import org.springframework.stereotype.Component;

@Component
class JwtSettings {
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

//    @Value("${authentication.jwt.claimNames.refreshCount}")
    private String refreshCountClaimName = "refreshCount";

//    @Value("${authentication.jwt.claimNames.refreshLimit}")
    private String refreshLimitClaimName = "refreshLimit";

    public String getSecret() {
        return secret;
    }

    public Long getClockSkew() {
        return clockSkew;
    }

    public String getAudience() {
        return audience;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthoritiesClaimName() {
        return authoritiesClaimName;
    }

    public String getRefreshCountClaimName() {
        return refreshCountClaimName;
    }

    public String getRefreshLimitClaimName() {
        return refreshLimitClaimName;
    }
}

