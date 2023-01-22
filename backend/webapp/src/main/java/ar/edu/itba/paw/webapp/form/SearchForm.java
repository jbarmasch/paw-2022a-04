package ar.edu.itba.paw.webapp.form;

public class SearchForm {
    private String query;
    private boolean byUsername;
    private String username;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isByUsername() {
        return byUsername;
    }

    public void setByUsername(boolean byUsername) {
        this.byUsername = byUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
