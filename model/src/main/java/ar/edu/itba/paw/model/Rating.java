package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
@IdClass(RatingId.class)
public class Rating {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    private User user;
    
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizerid")
    private User organizer;
    
    private int rating;

    public Rating(User user, User organizer, int rating) {
        this.user = user;
        this.organizer = organizer;
        this.rating = rating;
    }

    public Rating() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
