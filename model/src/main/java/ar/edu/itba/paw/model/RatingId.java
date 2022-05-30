package ar.edu.itba.paw.model;

import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {
    private User user;
    private User organizer;

    public RatingId(User user, User organizer) {
        this.user = user;
        this.organizer = organizer;
    }

    RatingId() {}

    public User getUser() {
        return user;
    }

    public User getOrganizer() {
        return organizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingId)) return false;
        RatingId ratingId = (RatingId) o;
        return Objects.equals(user, ratingId.user) && Objects.equals(organizer, ratingId.organizer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, organizer);
    }
}
