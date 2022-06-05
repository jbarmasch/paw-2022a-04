package ar.edu.itba.paw.model;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "eventbookings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userid", "eventid"}),
 }
)
public class EventBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventbookings_id_seq")
    @SequenceGenerator(sequenceName = "eventbookings_id_seq", name = "eventbookings_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "eventid")
    private Event event;

    @OneToMany(mappedBy = "eventBooking", fetch = FetchType.EAGER)
    @Where(clause = "qty > 0")
    private List<TicketBooking> ticketBookings;

    @Column(length = 100, unique = true)
    private String code;

    @Formula("(select r.rating from ratings r join events e on r.organizerid = e.userid where r.userid = userid and e.eventid = eventid)")
    private Integer rating;

    @Column(nullable = false)
    private boolean confirmed;

    public EventBooking(User user, Event event, List<TicketBooking> ticketBookings, String code, Integer rating) {
        this.user = user;
        this.event = event;
        this.ticketBookings = ticketBookings;
        this.code = code;
        this.rating = rating;
    }

    public EventBooking(User user, Event event, List<TicketBooking> ticketBookings, String code) {
        this.user = user;
        this.event = event;
        this.ticketBookings = ticketBookings;
        this.code = code;
        this.confirmed = false;
    }

    public EventBooking(User user, Event event, List<TicketBooking> ticketBookings, String code, boolean confirmed) {
        this.user = user;
        this.event = event;
        this.ticketBookings = ticketBookings;
        this.code = code;
        this.confirmed = confirmed;
    }

    public EventBooking() {}

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<TicketBooking> getTicketBookings() {
        return ticketBookings;
    }

    public void setTicketBookings(List<TicketBooking> ticketBookings) {
        this.ticketBookings = ticketBookings;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void addBooking(TicketBooking ticketBooking) {
        ticketBookings.add(ticketBooking);
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public int getTicketBookingsSize() {
        return ticketBookings.size();
    }
}
