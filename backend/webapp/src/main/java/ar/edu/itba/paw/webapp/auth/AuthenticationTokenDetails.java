package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.RoleEnum;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class AuthenticationTokenDetails {
    private final String id;
    private final String username;
    private final Set<RoleEnum> authorities;
    private final ZonedDateTime issuedDate;
    private final ZonedDateTime expirationDate;
    private final boolean isRefresh;

    private AuthenticationTokenDetails(String id, String username, Set<RoleEnum> authorities, ZonedDateTime issuedDate, ZonedDateTime expirationDate, boolean isRefresh) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.issuedDate = issuedDate;
        this.expirationDate = expirationDate;
        this.isRefresh = isRefresh;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Set<RoleEnum> getAuthorities() {
        return authorities;
    }

    public ZonedDateTime getIssuedDate() {
        return issuedDate;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public static class Builder {
        private String id;
        private String username;
        private Set<RoleEnum> authorities;
        private ZonedDateTime issuedDate;
        private ZonedDateTime expirationDate;
        private boolean isRefresh;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withAuthorities(Set<RoleEnum> authorities) {
            this.authorities = Collections.unmodifiableSet(authorities == null ? new HashSet<>() : authorities);
            return this;
        }

        public Builder withIssuedDate(ZonedDateTime issuedDate) {
            this.issuedDate = issuedDate;
            return this;
        }

        public Builder withExpirationDate(ZonedDateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder withIsRefresh(boolean isRefresh) {
            this.isRefresh = isRefresh;
            return this;
        }


        public AuthenticationTokenDetails build() {
            return new AuthenticationTokenDetails(id, username, authorities, issuedDate, expirationDate, isRefresh);
        }
    }
}
