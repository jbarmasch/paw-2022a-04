package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EventFinishedException;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EventJpaDao implements EventDao {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ImageDao imageDao;

    @Override
    public Event createEvent(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, User organizer, Integer minAge, User bouncer) {
        Location location = em.getReference(Location.class, locationId);
        Type type = em.getReference(Type.class, typeId);
        Image image = imageDao.createImage(imageArray);
        List<Tag> tagList = new ArrayList<>();
        for (Long tagId : tagIds)
            tagList.add(em.getReference(Tag.class, tagId));
        final Event event = new Event(name, description, location, type, date, tagList, organizer, State.ACTIVE, null, image, minAge, bouncer);
        em.persist(event);
        return event;
    }

    @Override
    public void updateEventImage(long id, byte[] imageArray) {
        Image image = imageDao.createImage(imageArray);
        final Event event = em.find(Event.class, id);
        event.setImage(image);
        em.persist(event);
    }

    @Override
    public Optional<Event> getEventById(long id) {
        return Optional.ofNullable(em.find(Event.class, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public EventList filterBy(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String searchQuery, List<Integer> tags, String username, Long userId, Order order, Boolean showSoldOut, int page) {
        boolean having = false, condition = false;
        Map<String, Object> objects = new HashMap<>();
        objects.put("date", Timestamp.valueOf(LocalDateTime.now()));
        StringBuilder querySelect = new StringBuilder("FROM events ec");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > :date");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (locations != null && locations.size() > 0) {
            queryCondition.append(" AND locationid IN :locationids");
            objects.put("locationids", locations);
        }
        System.out.println(types != null ? types.size() : "0");
        if (types != null && types.size() > 0) {
            queryCondition.append(" AND typeid IN :typeids");
            objects.put("typeids", types);
        }
        if (searchQuery != null) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', ec.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR ec.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }
        if (username != null) {
            querySelect.append(" JOIN users u ON ec.userid = u.userid");
            queryCondition.append(" AND username = :username");
            objects.put("username", username);
        } else if (userId != null) {
            queryCondition.append(" AND userId = :userId");
            objects.put("userId", userId);
        }

        queryCondition.append(" GROUP BY ec.eventid, date ");
        StringBuilder orderQuery = new StringBuilder();
        if (order != null) {
            orderQuery.append(" ORDER BY ").append(order.getOrder()).append(" ").append(order.getOrderBy());
        } else {
            orderQuery.append(" ORDER BY date ");
        }

        querySelect.append(" LEFT JOIN tickets t on ec.eventid = t.eventid");

        if (minPrice != null) {
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
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(t.price), 0) <= :maxPrice");
            objects.put("maxPrice", maxPrice);
        }
        if (tags != null && tags.size() > 0) {
            querySelect.append(" LEFT JOIN eventtags e on ec.eventid = e.eventid");
            if (!having)
                queryCondition.append(" HAVING");
            else
                queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(e.tagid) && ARRAY");
            queryCondition.append(tags);
        }
        objects.put("page", (page - 1) * 8);
        String query = "SELECT DISTINCT ec.eventid " + querySelect.append(queryCondition);
        System.out.println(query);
        Query queryNative = em.createNativeQuery(query + " LIMIT 8 OFFSET :page");
        objects.forEach(queryNative::setParameter);
        final List<Long> ids = (List<Long>) queryNative.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new EventList(new ArrayList<>(), 0);
        final TypedQuery<Event> typedQuery = em.createQuery("from Event where eventid IN :ids " + orderQuery, Event.class);
        typedQuery.setParameter("ids", ids);

        String theQuery = String.valueOf(querySelect);
        theQuery = theQuery.replace("GROUP BY ec.eventid, date", "");
        Query count = em.createNativeQuery("SELECT COUNT(DISTINCT ec.eventid) " + theQuery);
        objects.remove("page");
        objects.forEach(count::setParameter);
        return new EventList(typedQuery.getResultList(), (int) Math.ceil((double) ((Number) count.getSingleResult()).intValue() / 8));
    }

    @Override
    public void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge) {
        Location location = em.getReference(Location.class, locationId);
        Type type = em.getReference(Type.class, typeId);
        Image image = imageDao.createImage(imageArray);
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
    public void soldOut(long id) {
        final Event event = em.find(Event.class, id);
        if (event.isFinished())
            throw new EventFinishedException();
        event.setState(State.SOLDOUT);
        em.persist(event);
    }

    @Override
    public void active(long id) {
        final Event event = em.find(Event.class, id);
        if (event.isFinished())
            throw new EventFinishedException();
        event.setState(State.ACTIVE);
        em.persist(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getFewTicketsEvents() {
        Query idQuery = em.createNativeQuery("SELECT aux.eventid FROM " +
                "(SELECT events.eventid, events.name, events.description, events.locationid, SUM(COALESCE(ti.booked, 0)) AS attendance, " +
                "MIN(CASE WHEN ti.qty - ti.booked > 0 THEN ti.price END) AS minPrice, (SUM(COALESCE(ti.qty, 0)) - SUM(COALESCE(ti.booked, 0))) " +
                "AS ticketsLeft, events.typeid, events.date, events.imageid, events.userid, events.state, locations.name AS locName, types.name AS typeName, " +
                "users.username, ARRAY_AGG(ti.ticketId) AS ticketIds, ARRAY_AGG(ti.qty) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, " +
                "ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.price) AS ticketPrices FROM events JOIN locations ON events.locationid = locations.locationid " +
                "LEFT OUTER JOIN tickets ti ON events.eventid = ti.eventid JOIN types ON events.typeid = types.typeid JOIN users ON events.userid = users.userid " +
                "GROUP BY events.eventId, locations.locationid, types.typeid, users.username) AS aux LEFT OUTER JOIN eventTags eT ON aux.eventId = eT.eventId " +
                "LEFT OUTER JOIN tags t ON eT.tagId = t.tagId WHERE date > :date AND attendance >= (4 * ticketsLeft) AND state != 1 AND state != 2 AND ticketsLeft > 0 LIMIT 4");
        idQuery.setParameter("date", Timestamp.valueOf(LocalDateTime.now()));
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
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
                "MIN(CASE WHEN ti.qty - ti.booked > 0 THEN ti.price END) AS minPrice, (SUM(COALESCE(ti.qty, 0)) - SUM(COALESCE(ti.booked, 0))) " +
                "AS ticketsLeft, events.typeid, events.date, events.imageid, events.userid, events.state, locations.name AS locName, types.name AS typeName, " +
                "users.username, ARRAY_AGG(ti.ticketId) AS ticketIds, ARRAY_AGG(ti.qty) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, " +
                "ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.price) AS ticketPrices FROM events JOIN locations ON events.locationid = locations.locationid " +
                "LEFT OUTER JOIN tickets ti ON events.eventid = ti.eventid JOIN types ON events.typeid = types.typeid JOIN users ON events.userid = users.userid " +
                "GROUP BY events.eventId, locations.locationid, types.typeid, users.username) AS aux LEFT OUTER JOIN eventTags eT ON aux.eventId = eT.eventId " +
                "LEFT OUTER JOIN tags t ON eT.tagId = t.tagId WHERE date > :date AND state != 1 AND state != 2 ORDER BY date LIMIT 4");
        idQuery.setParameter("date", Timestamp.valueOf(LocalDateTime.now()));
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
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
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
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
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new ArrayList<>();
        final TypedQuery<Event> query = em.createQuery("from Event where eventid IN :ids", Event.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getUserEvents(long id, int page) {
        Query idQuery = em.createNativeQuery("SELECT eventId FROM events WHERE userid = :userid AND state != 1 AND date > NOW() ORDER BY date LIMIT 10 OFFSET :offset");
        idQuery.setParameter("userid", id);
        idQuery.setParameter("offset", (page - 1) * 10);
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new ArrayList<>();
        final TypedQuery<Event> query = em.createQuery("from Event where eventid IN :ids ORDER BY date", Event.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<EventStats> getEventStats(long id) {
        Query query = em.createNativeQuery("SELECT eventName, bookings AS attended, expectedBooked AS booked, CAST(bookings AS numeric) / COALESCE(NULLIF(expectedBooked, 0), 1) " +
                "AS attendance, CAST(bookings AS numeric) / COALESCE(NULLIF(qty, 0), 1) AS saleRatio, income, expectedIncome FROM (SELECT eventName, SUM(realqty) AS bookings, SUM(booked) " +
                "AS expectedBooked, SUM(qty) AS qty, SUM(price * realqty) AS income, SUM(price * booked) AS expectedIncome FROM (SELECT booked, price, " +
                "SUM(CASE WHEN confirmed THEN tb.qty ELSE 0 END) AS realqty, t.qty AS qty, e.name AS eventName FROM events e LEFT JOIN tickets t ON e.eventId = t.eventId LEFT " +
                "JOIN ticketbookings tb on t.ticketId = tb.ticketId JOIN eventbookings eb on tb.id = eb.id WHERE e.eventid = :eventid " +
                "GROUP BY t.ticketid, e.eventid) AS aux GROUP BY eventName) AS general");
        query.setParameter("eventid", id);
        List<Object[]> resultSet = query.getResultList();
        if (resultSet.isEmpty())
            return Optional.empty();
        Object[] result = resultSet.get(0);
        if (result == null)
            return Optional.empty();
        return Optional.of(new EventStats((String) result[0], ((Number) result[1]).intValue(), ((Number) result[2]).intValue(), ((Number) result[3]).doubleValue(),
                ((Number) result[4]).doubleValue(), ((Number) result[5]).doubleValue(), ((Number) result[6]).doubleValue()));
    }
}
