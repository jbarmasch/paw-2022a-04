package ar.edu.itba.paw.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid_seq")
    @SequenceGenerator(sequenceName = "users_userid_seq", name = "users_userid_seq", allocationSize = 1)
    @Column(name = "userid")
    private long id;
    @Column(length = 100, nullable = false, unique = true)
    private String username;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 100, nullable = false, unique = true)
    private String mail;

    @Formula("(select coalesce(avg(r.rating), 0) from ratings r where r.organizerid = userid)")
    private double rating;
    @Formula("(select count(coalesce(r.rating, 0)) from ratings r where r.organizerid = userid)")
    private int votes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "userroles",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private List<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organizer")
    @Where(clause = "state != 2")
    private List<Event> events;

    public User(String username, String password, String mail, double rating, int votes, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.rating = rating;
        this.votes = votes;
        this.roles = roles;
    }

    public User(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public User() {}

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

    public double getRating() {
        return rating;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public int getVotes() {
        return votes;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
