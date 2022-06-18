package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
//@Table(name = "tickets", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"eventid", "name"})
//})
@Table(name = "tickets")
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
    List<TicketBooking> ticketBookings;

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
        String dateStr = starting.toString();
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(5, 7);
        String day = dateStr.substring(8, 10);
        return day + "/" + month + "/" + year;
    }

    public String getStartingTimeFormatted() {
        return starting.toString().substring(11, 16);
    }

    public String getUntilDateFormatted() {
        String dateStr = until.toString();
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(5, 7);
        String day = dateStr.substring(8, 10);
        return day + "/" + month + "/" + year;
    }

    public String getUntilTimeFormatted() {
        return until.toString().substring(11, 16);
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

