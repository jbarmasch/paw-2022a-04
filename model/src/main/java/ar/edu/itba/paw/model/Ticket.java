package ar.edu.itba.paw.model;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"eventid", "name"})
})
@Check(constraints = "maxperuser >= 1 AND maxperuser <= 10")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tickets_ticketid_seq")
    @SequenceGenerator(sequenceName = "tickets_ticketid_seq", name = "tickets_ticketid_seq", allocationSize = 1)
    @Column(name = "ticketid")
    private long id;

    @Column(name = "name", length = 100)
    private String ticketName;
    private Double price;
    private Integer qty;
    private Integer booked;
    private Integer maxPerUser;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "eventid")
    private Event event;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "ticket")
    private List<TicketBooking> ticketBookings;

    private LocalDateTime starting;
    private LocalDateTime until;

    public Ticket(String ticketName, Double price, Integer qty, Event event, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) {
        this.ticketName = ticketName;
        this.price = price;
        this.qty = qty;
        this.booked = 0;
        this.event = event;
        this.starting = starting;
        this.until = until;
        this.maxPerUser = maxPerUser;
    }

    public Ticket(String ticketName, Double price, Integer qty, Event event, Integer maxPerUser) {
        this.ticketName = ticketName;
        this.price = price;
        this.qty = qty;
        this.booked = 0;
        this.event = event;
        this.maxPerUser = maxPerUser;
    }

    public Ticket() {}

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getTicketName() {
        return ticketName;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQty() {
        return qty;
    }

    public Integer getMaxPerUser() {
        return maxPerUser;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public void setMaxPerUser(Integer maxPerUser) {
        this.maxPerUser = maxPerUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getBooked() {
        return booked;
    }

    public void setBooked(Integer booked) {
        this.booked = booked;
    }

    public Integer getTicketsLeft() {
        return qty - booked;
    }

    public boolean book(int book) {
        if (booked + book > qty)
            return false;
        booked += book;
        return true;
    }

    public boolean cancelBooking(int cancel) {
        if (booked - cancel < 0)
            return false;
        booked -= cancel;
        return true;
    }

    public LocalDateTime getStarting() {
        return starting;
    }

    public void setStarting(LocalDateTime starting) {
        this.starting = starting;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }

    public String getStartingDateFormatted() {
        return DateUtils.getDateFormatted(starting);
    }

    public String getStartingTimeFormatted() {
        return DateUtils.getTimeFormatted(starting);
    }

    public String getUntilDateFormatted() {
        return DateUtils.getDateFormatted(until);
    }

    public String getUntilTimeFormatted() {
        return DateUtils.getTimeFormatted(until);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

