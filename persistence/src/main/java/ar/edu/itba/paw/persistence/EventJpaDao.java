package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EventJpaDao implements EventDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, String bouncerPass) {
        Location location = em.getReference(Location.class, locationId);
        Type type = em.getReference(Type.class, typeId);
        Image image;
        if (imageArray != null) {
            image = new Image(imageArray);
            em.persist(image);
        } else
            image = em.getReference(Image.class, 1L);
        List<Tag> tagList = new ArrayList<>();
        for (Long tagId : tagIds)
            tagList.add(em.getReference(Tag.class, tagId));
        User user = em.getReference(User.class, userId);
        user.addRole(em.getReference(Role.class, RoleEnum.CREATOR.ordinal() + 1));
        em.persist(user);
        User bouncer = new User(null, bouncerPass, null, Collections.singletonList(em.getReference(Role.class, RoleEnum.BOUNCER.ordinal() + 1)));
        final Event event = new Event(name, description, location, type, date, tagList, user, State.ACTIVE, null, image, minAge, bouncer);
        em.persist(event);
        bouncer.setUsername(String.valueOf(event.getId()));
        bouncer.setMail(String.valueOf(event.getId()));
        em.persist(bouncer);
        return event;
    }

    @Override
    public Optional<Event> getEventById(long id) {
        return Optional.ofNullable(em.find(Event.class, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String searchQuery, Integer[] tags, String username, Order order, Boolean showSoldOut, int page) {
        boolean having = false, condition = false;
        Map<String, Object> objects = new HashMap<>();
        objects.put("date", Timestamp.valueOf(LocalDateTime.now()));
        StringBuilder querySelect = new StringBuilder("SELECT ec.eventid FROM events ec");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > :date");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (locations != null && locations.length > 0) {
            queryCondition.append(" AND locationid IN :locationids");
            objects.put("locationids", Arrays.asList(locations));
        }
        if (types != null && types.length > 0) {
            queryCondition.append(" AND typeid IN :typeids");
            objects.put("typeids", Arrays.asList(types));
        }
        if (searchQuery != null) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', ec.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR ec.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }
        if (username != null) {
            querySelect.append(" JOIN users u ON ec.userid = u.userid");
            queryCondition.append(" AND username = :username");
            objects.put("username", username);
        }
        queryCondition.append(" GROUP BY ec.eventid");
        if (minPrice != null) {
            querySelect.append(" LEFT JOIN tickets t on ec.eventid = t.eventid");
            condition = true;
            queryCondition.append(" HAVING");
            having = true;
            queryCondition.append(" COALESCE(MIN(t.price), 0) >= :minPrice");
            objects.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            if (!condition)
                querySelect.append(" LEFT JOIN tickets t on ec.eventid = t.eventid");
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            }
            else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(t.price), 0) <= :maxPrice");
            objects.put("maxPrice", maxPrice);
        }
        if (tags != null && tags.length > 0) {
            querySelect.append(" LEFT JOIN eventtags e on ec.eventid = e.eventid");
            if (!having)
                queryCondition.append(" HAVING");
            else
                queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(e.tagid) @> ARRAY[:tagids]");
            objects.put("tagids", Arrays.asList(tags));
        }
        objects.put("page", (page - 1) * 10);
        queryCondition.append(" LIMIT 10 OFFSET :page");
        StringBuilder query = querySelect.append(queryCondition);
        Query queryNative = em.createNativeQuery(String.valueOf(query));
        objects.forEach(queryNative::setParameter);
        final List<Long> ids = (List<Long>) queryNative.getResultList().stream().map(o -> ((Integer) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new ArrayList<>();

        StringBuilder orderQuery = new StringBuilder();
        if (order != null)
            orderQuery.append(" ORDER BY ").append(order.getOrder()).append(" ").append(order.getOrderBy());
        else
            orderQuery.append(" ORDER BY date ");

        final TypedQuery<Event> typedQuery = em.createQuery("from Event where eventid IN :ids " + orderQuery, Event.class);
        typedQuery.setParameter("ids", ids);
        return typedQuery.getResultList();
    }

    @Override
    public void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge) {
        Location location = em.getReference(Location.class, locationId);
        Type type = em.getReference(Type.class, typeId);
        Image image;
        if (imageArray != null) {
            image = new Image(imageArray);
            em.persist(image);
        } else
            image = em.getReference(Image.class, 1L);
        List<Tag> tagList = new ArrayList<>();
        for (Long tagId : tagIds)
            tagList.add(em.getReference(Tag.class, tagId));
        final Event event = em.find(Event.class, id);
        event.setDate(date);
        event.setDescription(description);
        event.setImage(image);
        event.setLocation(location);
        event.setTags(tagList);
        event.setType(type);
        event.setName(name);
        event.setMinAge(minAge);
        em.persist(event);
    }

    @Override
    public void deleteEvent(long id) {
        final Event event = em.find(Event.class, id);
        event.setState(State.DELETED);
        em.persist(event);
    }

    @Override
    public EventBooking book(EventBooking booking) {
        if (booking.getEvent().isFinished())
            return null;

        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid and eb.event.id = :eventid", EventBooking.class);
        query.setParameter("eventid", booking.getEvent().getId());
        query.setParameter("userid", booking.getUser().getId());
        EventBooking eventBooking = query.getResultList().stream().findFirst().orElse(null);

        System.out.println("evb: " + eventBooking);
        System.out.println("uid: " + booking.getUser().getId() + " eid: " + booking.getEvent().getId());

        if (eventBooking == null) {
            for (TicketBooking ticketBooking : booking.getTicketBookings()) {
                if (ticketBooking.getQty() != null && ticketBooking.getQty() > 0) {
                    Ticket ticket = ticketBooking.getTicket();
                    if (!ticket.book(ticketBooking.getQty())) {
                        // TODO
                        // throw new RuntimeException();
                        return null;
                    }
                    em.persist(ticket);
                    em.persist(ticketBooking);
                }
            }
            em.persist(booking);
            return booking;
        } else {
            for (TicketBooking ticketBooking : booking.getTicketBookings()) {
                if (ticketBooking.getTicket().getEvent().getId() != booking.getEvent().getId()) {
                    // TODO
                    // throw new RuntimeException();
                    return null;
                }

                TicketBooking ticketAux = em.find(TicketBooking.class, new BookingId(ticketBooking.getTicket(), eventBooking));
                if (ticketAux != null) {
                    Ticket ticket = ticketAux.getTicket();
                    if (!ticket.book(ticketBooking.getQty())) {
                        // TODO
                        // throw new RuntimeException();
                        return null;
                    }
                    em.persist(ticket);
                    if (ticketAux.getQty() != null && ticketAux.getQty() > 0) {
                        ticketAux.setQty(ticketAux.getQty() + ticketBooking.getQty());
                        em.persist(ticketAux);
                    }
                }
                else {
                    Ticket ticket = ticketBooking.getTicket();
                    if (!ticket.book(ticketBooking.getQty())) {
                        // TODO
                        // throw new RuntimeException();
                        return null;
                    }
                    em.persist(ticket);
                    ticketBooking.setEventBooking(eventBooking);
                    eventBooking.addBooking(ticketBooking);
                    em.persist(ticketBooking);
                    System.out.println("1ro aca");
                }
            }
            System.out.println(eventBooking.getCode());
            em.persist(eventBooking);

            System.out.println("susana");
            return eventBooking;
        }
    }

    @Override
    public boolean cancelBooking(EventBooking booking) {
        if (booking.getEvent().isFinished())
            return false;

        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid and eb.event.id = :eventid", EventBooking.class);
        query.setParameter("userid", booking.getUser().getId());
        query.setParameter("eventid", booking.getEvent().getId());
        EventBooking eventBooking = query.getResultList().stream().findFirst().orElse(null);

        if (eventBooking == null) {
            // TODO
            // throw new RuntimeException();
            return false;
        }

        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            TicketBooking tb = em.find(TicketBooking.class, new BookingId(ticketBooking.getTicket(), eventBooking));
            if (tb == null) {
                // TODO
                // throw new RuntimeException();
                return false;
            }
            Ticket ticket = ticketBooking.getTicket();
            if (!ticket.cancelBooking(ticketBooking.getQty())) {
                // TODO
                // throw new RuntimeException();
                return false;
            }
            em.persist(ticket);
            tb.setQty(tb.getQty() - ticketBooking.getQty());
            em.persist(tb);
        }
        em.persist(eventBooking);
        return true;
    }

    @Override
    public void soldOut(long id) {
        final Event event = em.find(Event.class, id);
        event.setState(State.SOLDOUT);
        em.persist(event);
    }

    @Override
    public void active(long id) {
        final Event event = em.find(Event.class, id);
        event.setState(State.ACTIVE);
        em.persist(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getFewTicketsEvents() {
        Query idQuery = em.createNativeQuery("SELECT aux.eventid FROM " +
                "(SELECT events.eventid, events.name, events.description, events.locationid, SUM(COALESCE(ti.booked, 0)) AS attendance, " +
                "MIN(CASE WHEN ti.maxTickets - ti.booked > 0 THEN ti.price END) AS minPrice, (SUM(COALESCE(ti.maxTickets, 0)) - SUM(COALESCE(ti.booked, 0))) " +
                "AS ticketsLeft, events.typeid, events.date, events.imageid, events.userid, events.state, locations.name AS locName, types.name AS typeName, " +
                "users.username, ARRAY_AGG(ti.ticketId) AS ticketIds, ARRAY_AGG(ti.maxTickets) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, " +
                "ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.price) AS ticketPrices FROM events JOIN locations ON events.locationid = locations.locationid " +
                "LEFT OUTER JOIN tickets ti ON events.eventid = ti.eventid JOIN types ON events.typeid = types.typeid JOIN users ON events.userid = users.userid " +
                "GROUP BY events.eventId, locations.locationid, types.typeid, users.username) AS aux LEFT OUTER JOIN eventTags eT ON aux.eventId = eT.eventId " +
                "LEFT OUTER JOIN tags t ON eT.tagId = t.tagId WHERE date > :date AND attendance >= (4 * ticketsLeft) AND state != 1 AND state != 2 AND ticketsLeft > 0 LIMIT 4");
        idQuery.setParameter("date", Timestamp.valueOf(LocalDateTime.now()));
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Integer) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new ArrayList<>();
        final TypedQuery<Event> query = em.createQuery("from Event where eventid IN :ids", Event.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getUpcomingEvents() {
        Query idQuery = em.createNativeQuery("SELECT aux.eventid FROM " +
                "(SELECT events.eventid, events.name, events.description, events.locationid, SUM(COALESCE(ti.booked, 0)) AS attendance, " +
                "MIN(CASE WHEN ti.maxTickets - ti.booked > 0 THEN ti.price END) AS minPrice, (SUM(COALESCE(ti.maxTickets, 0)) - SUM(COALESCE(ti.booked, 0))) " +
                "AS ticketsLeft, events.typeid, events.date, events.imageid, events.userid, events.state, locations.name AS locName, types.name AS typeName, " +
                "users.username, ARRAY_AGG(ti.ticketId) AS ticketIds, ARRAY_AGG(ti.maxTickets) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, " +
                "ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.price) AS ticketPrices FROM events JOIN locations ON events.locationid = locations.locationid " +
                "LEFT OUTER JOIN tickets ti ON events.eventid = ti.eventid JOIN types ON events.typeid = types.typeid JOIN users ON events.userid = users.userid " +
                "GROUP BY events.eventId, locations.locationid, types.typeid, users.username) AS aux LEFT OUTER JOIN eventTags eT ON aux.eventId = eT.eventId " +
                "LEFT OUTER JOIN tags t ON eT.tagId = t.tagId WHERE date > :date AND state != 1 AND state != 2 ORDER BY date LIMIT 4");
        idQuery.setParameter("date", Timestamp.valueOf(LocalDateTime.now()));
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Integer) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new ArrayList<>();
        final TypedQuery<Event> query = em.createQuery("from Event where eventid IN :ids", Event.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getSimilarEvents(long eventId) {
        Query idQuery = em.createNativeQuery("WITH event_cte AS (SELECT e.eventid, name, description, locationid, date, typeid, userid, " +
                "imageid, state, ARRAY_AGG(tagId) AS tagIds FROM events e LEFT OUTER JOIN eventTags eT ON e.eventId = eT.eventId WHERE e.eventId = :eventid " +
                "GROUP BY e.eventid, name, description, locationid, date, typeid, userid, imageid, state) SELECT eventid FROM (SELECT *, (SELECT COUNT(*) " +
                "FROM (SELECT unnest((SELECT tagIds FROM event_cte)) INTERSECT SELECT unnest(tagIds)) AS aux) AS similarity FROM (SELECT e.eventid, " +
                "e.state, ARRAY_AGG(tagId) AS tagIds, date FROM events e LEFT OUTER JOIN eventTags eT ON e.eventId = eT.eventId WHERE e.typeId = (SELECT " +
                "typeid FROM event_cte) AND e.locationId = (SELECT locationid FROM event_cte) AND e.eventid <> (SELECT eventid FROM event_cte) GROUP BY " +
                "e.eventid) as eliteTt WHERE state != 1 AND state != 2 AND date > :date ORDER BY similarity DESC LIMIT 5) AS aux");
        idQuery.setParameter("eventid", eventId);
        idQuery.setParameter("date", Timestamp.valueOf(LocalDateTime.now()));
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Integer) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new ArrayList<>();
        final TypedQuery<Event> query = em.createQuery("from Event where eventid IN :ids", Event.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getPopularEvents(long eventId) {
        Query idQuery = em.createNativeQuery("SELECT e.eventid FROM (SELECT b.eventId, COUNT(b.userId) AS popularity " +
                "FROM eventbookings b JOIN (SELECT userId FROM eventbookings eb WHERE eb.eventId = :eventid) AS aux ON b.userId = aux.userId " +
                "WHERE b.eventid <> :eventid GROUP BY b.eventId ORDER BY popularity DESC) AS aux " +
                "JOIN events e on aux.eventid = e.eventid WHERE state != 1 AND state != 2 AND date > :date LIMIT 5");
        idQuery.setParameter("eventid", eventId);
        idQuery.setParameter("date", Timestamp.valueOf(LocalDateTime.now()));
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Integer) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new ArrayList<>();
        final TypedQuery<Event> query = em.createQuery("from Event where eventid IN :ids", Event.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public void addTicket(long eventId, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until) {
        final Event event = em.find(Event.class, eventId);
        Ticket newTicket = new Ticket(ticketName, price, qty, event, starting, until);
        event.addTicket(newTicket);
        em.persist(event);
        em.persist(newTicket);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return Optional.ofNullable(em.find(Ticket.class, ticketId));
    }

    @Override
    public void updateTicket(long id, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until) {
        final Ticket ticket = em.find(Ticket.class, id);
        ticket.setTicketName(ticketName);
        ticket.setPrice(price);
        ticket.setQty(qty);
        ticket.setStarting(starting);
        ticket.setUntil(until);
        em.persist(ticket);
    }

    @Override
    public void deleteTicket(long ticketId) {
        em.remove(em.find(Ticket.class, ticketId));
    }

    @Override
    public List<TicketBooking> getTicketBookings(long ticketId) {
        final TypedQuery<TicketBooking> query = em.createQuery("from TicketBooking where :ticketid = ticketid", TicketBooking.class);
        query.setParameter("ticketid", ticketId);
        return query.getResultList();
    }
}
