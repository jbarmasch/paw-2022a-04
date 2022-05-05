package ar.edu.itba.paw.model;

public class User {
    private final int id;
    private String username;
    private String password;
    private String mail;
    private double rating;

    public User(int id, String username, String password, String mail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public User(int id, String username, String password, String mail, double rating) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.rating = rating;
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
}
