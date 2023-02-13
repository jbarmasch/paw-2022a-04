package ar.edu.itba.paw.model;

import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@Where(clause = "state != 1")
@Check(constraints = "minage >= 14 AND minage <= 27")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_eventid_seq")
    @SequenceGenerator(sequenceName = "events_eventid_seq", name = "events_eventid_seq", allocationSize = 1)
    @Column(name = "eventid")
    private long id;
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "description", length = 100)
    private String description;
    private LocalDateTime date;

    @Formula("(select coalesce(min(t.price), -1) from tickets t where t.eventid = eventid and (t.starting IS NULL OR t.starting <= NOW()) and (t.until IS NULL OR t.until >= NOW()) AND t.qty - t.booked > 0)")
    private double minPrice;
    @Formula("(select coalesce(sum(t.booked), 0) from tickets t where t.eventid = eventid)")
    private int attendance;
    @Formula("(select coalesce(sum(t.qty), 0) from tickets t where t.eventid = eventid)")
    private int maxCapacity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "imageid")
    private Image image;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "locationid")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "typeid")
    private Type type;

    @Column(name = "minage")
    private Integer minAge;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "eventtags",
            joinColumns = @JoinColumn(name = "eventid"),
            inverseJoinColumns = @JoinColumn(name = "tagid")
    )
    private List<Tag> tags;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bouncerid")
    private User bouncer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Ticket> tickets;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid")
    private User organizer;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    public Event(long id, String name, String description, Location location, Type type, LocalDateTime date, List<Tag> tags, User organizer,
                 State state, List<Ticket> tickets, Image image, Integer minAge, User bouncer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.type = type;
        this.tags = tags;
        this.organizer = organizer;
        this.state = state;
        this.tickets = tickets;
        this.image = image;
        this.minAge = minAge;
        this.bouncer = bouncer;
    }

    public Event(String name, String description, Location location, Type type, LocalDateTime date, List<Tag> tags, User organizer,
                 State state, List<Ticket> tickets, Image image, Integer minAge, User bouncer) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.type = type;
        this.tags = tags;
        this.organizer = organizer;
        this.state = state;
        this.tickets = tickets;
        this.image = image;
        this.minAge = minAge;
        this.bouncer = bouncer;
    }

    public Event() {
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDateFormatted() {
        return DateUtils.getDateFormatted(date);
    }

    public String getTimeFormatted() {
        return DateUtils.getTimeFormatted(date);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean getSoldOut() {
        return state == State.SOLDOUT;
    }

    public boolean getDeleted() {
        return state == State.DELETED;
    }

    public boolean getActive() {
        return state == State.ACTIVE;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getTicketsSize() {
        return tickets.size();
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public User getBouncer() {
        return bouncer;
    }

    public void setBouncer(User bouncer) {
        this.bouncer = bouncer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isFinished() {
        return date.isBefore(LocalDateTime.now());
    }
}
