package ar.edu.itba.paw.model;

public class User {
    private long id;
    private String username;
    private String password;
    private String mail;

    public User(long id, String username, String password, String mail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }
}
