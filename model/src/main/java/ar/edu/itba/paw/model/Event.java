package ar.edu.itba.paw.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@FilterDefs({
      @FilterDef(
              name = "minPriceFilter",
              parameters = @ParamDef(name = "minPrice", type = "double")
      ),
      @FilterDef(
              name = "maxPriceFilter",
              parameters = @ParamDef(name = "minPrice", type = "double")
      ),
      @FilterDef(
              name = "locationFilter",
              parameters = @ParamDef(name = "locations", type = "int")
      ),

      @FilterDef(
              name = "typeFilter",
              parameters = @ParamDef(name = "types", type = "int")
      ),
      @FilterDef(
              name = "tagFilter",
              parameters = @ParamDef(name = "tags", type = "int")
      ),
      @FilterDef(
              name = "searchFilter",
              parameters = @ParamDef(name = "query", type = "string")
      ),
      @FilterDef(
              name = "soldOutFilter"
      )
})
@Filters({
     @Filter(
             name = "locationFilter",
             condition = "locationid IN (:locations)"
     ),
     @Filter(
             name = "typeFilter",
             condition = "typeid IN (:types)"
     ),
     @Filter(
             name = "tagFilter",
             condition = "tagids @> (:tags)"
     ),
     @Filter(
             name = "searchFilter",
             condition = "upper(name) LIKE '%' || upper(:query) || '%'"
     ),
     @Filter(
             name = "soldOutFilter",
             condition = "state <> 2"
     )
})
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

    @Formula("(select coalesce(min(t.price), 0) from tickets t where t.eventid = eventid)")
    private double minPrice;
    @Formula("(select coalesce(sum(t.booked), 0) from tickets t where t.eventid = eventid)")
    private int attendance;
    @Formula("(select coalesce(sum(t.maxtickets), 0) from tickets t where t.eventid = eventid)")
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    private List<Ticket> tickets;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid")
    private User organizer;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    public Event(String name, String description, Location location, Type type, LocalDateTime date, List<Tag> tags, User organizer,
                    State state, List<Ticket> tickets, Integer minAge) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.type = type;
        this.tags = tags;
        this.organizer = organizer;
        this.state = state;
        this.tickets = tickets;
        this.minAge = minAge;
    }

    public Event(String name, String description, Location location, Type type, LocalDateTime date, List<Tag> tags, User organizer,
                 State state, List<Ticket> tickets, Image image, Integer minAge) {
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
    }

    public Event() {}

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public State getState() {
        return state;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }

    public String getDateFormatted() {
        String dateStr = date.toString();
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(5, 7);
        String day = dateStr.substring(8, 10);
        return day + "/" + month + "/" + year;
    }

    public String getTimeFormatted() {
        return date.toString().substring(11, 16);
    }

    public List<Tag> getTags() {
        return tags;
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

    public int getTicketsSize() {
        return tickets.size();
    }

    public double getMinPrice() {
        return minPrice;
    }

    public int getAttendance() {
        return attendance;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public Image getImage() {
        return image;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
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
