package ar.edu.itba.paw.model;

import java.util.List;

public class User {
    private final int id;
    private String username;
    private String password;
    private String mail;
    private double rating;
    private List<Role> roles;

    public User(int id, String username, String password, String mail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public User(int id, String username, String password, String mail, double rating, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.rating = rating;
        this.roles = roles;
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public double getRating() {
        return rating;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
