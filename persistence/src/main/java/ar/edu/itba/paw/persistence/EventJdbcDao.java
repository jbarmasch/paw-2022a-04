package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.print.attribute.standard.JobOriginatingUserName;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class EventJdbcDao implements EventDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcTagInsert;
    private final SimpleJdbcInsert jdbcBookInsert;
    private final SimpleJdbcInsert jdbcTicketInsert;
    private final SimpleJdbcInsert jdbcRolesInsert;

    @Autowired
    public EventJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
        jdbcBookInsert = new SimpleJdbcInsert(ds).withTableName("bookings");
        jdbcTagInsert = new SimpleJdbcInsert(ds).withTableName("eventtags");
        jdbcRolesInsert = new SimpleJdbcInsert(ds).withTableName("userroles");
        jdbcTicketInsert = new SimpleJdbcInsert(ds).withTableName("tickets").usingGeneratedKeyColumns("ticketid");
    }

    @Override
    public Optional<Event> getEventById(long id, Locale locale) {
        return jdbcTemplate.query("SELECT * FROM event_complete" + JdbcUtils.getLocateExt(locale) + " WHERE eventId = ?", new Object[]{id}, JdbcUtils.EVENT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Event create(String name, String description, int locationId, int typeId, LocalDateTime date, int imageId, Integer[] tagIds, int userId, Locale locale) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("locationId", locationId);
        eventData.put("typeId", typeId);
        eventData.put("date", Timestamp.valueOf(date));
        eventData.put("imageId", imageId);
        eventData.put("userId", userId);
        eventData.put("attendance", 0);
        eventData.put("state", State.ACTIVE.ordinal());
        final int eventId = jdbcInsert.executeAndReturnKey(eventData).intValue();
        for (Integer tagId : tagIds)
            addTagToEvent(eventId, tagId);

        Integer roleCount = jdbcTemplate.query("SELECT COUNT(*) AS aux FROM userroles WHERE roleid = ? AND userid = ?",
                new Object[] { RoleEnum.CREATOR.ordinal() + 1, userId }, (rs, i) -> rs.getInt("aux")).stream().findFirst().orElse(null);
        if (roleCount == null || roleCount == 0) {
            final Map<String, Object> userRole = new HashMap<>();
            userRole.put("userid", userId);
            userRole.put("roleid", RoleEnum.CREATOR.ordinal() + 1);
            jdbcRolesInsert.execute(userRole);
        }

        return getEventById(eventId, locale).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Event> filterBy(Integer[] locations, Integer[] types , Double minPrice, Double maxPrice,
                                String searchQuery, Integer[] tags, String username, Order order, int page, Locale locale) {
        String localeExt = JdbcUtils.getLocateExt(locale);
        List<Object> objects = new ArrayList<>();
        objects.add(Timestamp.valueOf(LocalDateTime.now()));
        StringBuilder query = new StringBuilder("SELECT * FROM event_complete" + localeExt + " ec WHERE state != 1 AND date > ?");
        if (locations != null && locations.length > 0) {
            Integer lastLocation = locations[locations.length - 1];
            query.append(" AND locationid IN (");
            for (Integer location : locations) {
                query.append("?");
                objects.add(location);
                if (!Objects.equals(location, lastLocation))
                    query.append(", ");
            }
            query.append(")");
        }
        if (minPrice != null) {
            query.append(" AND minPrice >= ?");
            objects.add(minPrice);
        }
        if (maxPrice != null) {
            query.append(" AND minPrice <= ?");
            objects.add(maxPrice);
        }
        if (types != null && types.length > 0) {
            Integer lastType = types[types.length - 1];
            query.append(" AND typeid IN (");
            for (Integer type : types) {
                query.append("?");
                objects.add(type);
                if (!Objects.equals(type, lastType))
                    query.append(", ");
            }
            query.append(")");
        }
        if (tags != null && tags.length > 0) {
            Integer lastTag = tags[tags.length - 1];
//            query.append(" AND NOT EXISTS (SELECT true FROM UNNEST('{");
            query.append(" AND tagIds && ARRAY[");
            for (Integer tag : tags) {
                query.append("?");
                objects.add(tag);
                if (!Objects.equals(tag, lastTag))
                    query.append(", ");
            }
            query.append("]");
//            query.append("}'::INTEGER[]) t2 WHERE t2 NOT IN (SELECT UNNEST(tagIds) FROM event_complete WHERE ");
//            query.append("event_complete.eventid = ec.eventid)) AND ec.tagIds <> '{null}'");
        }
        if (searchQuery != null) {
            query.append(" AND ((SELECT to_tsvector('Spanish', name) @@ to_tsquery(?)) = 't' OR name ILIKE CONCAT('%', ?, '%'))");
            objects.add(searchQuery);
            objects.add(searchQuery);
        }
        if (username != null) {
            query.append(" AND username = ?");
            objects.add(username);
        }
        if (order != null) {
            query.append(" ORDER BY ").append(order.getOrder()).append(" ").append(order.getOrderBy());
        } else {
            query.append(" ORDER BY date ");
        }
        objects.add((page - 1) * 10);
        return jdbcTemplate.query(query + " LIMIT 10 OFFSET ?", objects.toArray(), JdbcUtils.EVENT_ROW_MAPPER);
    }

    @Override
    public List<Event> getAll(int page, Locale locale) {
        return jdbcTemplate.query("SELECT * FROM event_complete" + JdbcUtils.getLocateExt(locale) + " WHERE date > ? LIMIT 10 OFFSET ?",
                new Object[]{ Timestamp.valueOf(LocalDateTime.now()), (page - 1) * 10 }, JdbcUtils.EVENT_ROW_MAPPER);
    }

    @Override
    public List<Event> getFewTicketsEvents(Locale locale) {
        return jdbcTemplate.query("SELECT * FROM event_complete" + JdbcUtils.getLocateExt(locale) + " WHERE attendance >= (4 * ticketsLeft) AND state != 1 AND state != 2 " +
                "AND ticketsLeft > 0 AND date > ? LIMIT 4", new Object[]{ Timestamp.valueOf(LocalDateTime.now()) }, JdbcUtils.EVENT_ROW_MAPPER);
    }

    @Override
    public List<Event> getUpcomingEvents(Locale locale){
        return jdbcTemplate.query("SELECT * FROM event_complete" + JdbcUtils.getLocateExt(locale) + " WHERE date > ? AND state != 1 AND state != 2 ORDER BY date LIMIT 5",
                new Object[]{ Timestamp.valueOf(LocalDateTime.now()) }, JdbcUtils.EVENT_ROW_MAPPER);
    }

    @Override
    public void updateEvent(int id, String name, String description, Integer locationId, int ticketsLeft, double price,
                            int typeId, LocalDateTime date, int imgId, Integer[] tagIds) {
        if (imgId != 1) {
            jdbcTemplate.update("UPDATE events SET name = ?, description = ?, locationid = ?, ticketsLeft = ?, price = ?, typeid = ?, date = ?, imageid = ? WHERE eventid = ?",
                    name, description, locationId, ticketsLeft, price, typeId, Timestamp.valueOf(date), imgId, id);
        } else {
            jdbcTemplate.update("UPDATE events SET name = ?, description = ?, locationid = ?, ticketsLeft = ?, price = ?, typeid = ?, date = ? WHERE eventid = ?",
                    name, description, locationId, ticketsLeft, price, typeId, Timestamp.valueOf(date), id);
        }
        cleanTagsFromEvent(id);
        for (Integer tagId : tagIds) {
            addTagToEvent(id, tagId);
        }
    }

    @Override
    public void deleteEvent(int id) {
        jdbcTemplate.update("UPDATE events SET state = ? WHERE eventId = ?", State.DELETED.ordinal(), id);
    }

    @Override
    public void soldOut(int id) {
        jdbcTemplate.update("UPDATE events SET state = ? WHERE eventId = ?", State.SOLDOUT.ordinal(), id);
    }

    @Override
    public void active(int id) {
        jdbcTemplate.update("UPDATE events SET state = ? WHERE eventId = ?", State.ACTIVE.ordinal(), id);
    }

    @Override
    public List<Event> getUserEvents(long id, int page, Locale locale) {
        return jdbcTemplate.query("SELECT * FROM event_complete" + JdbcUtils.getLocateExt(locale) + " WHERE userid = ? LIMIT 10 OFFSET ?", new Object[]{id, (page - 1) * 10}, JdbcUtils.EVENT_ROW_MAPPER);
    }

    private void addTagToEvent(int eventId, int tagId) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventId", eventId);
        eventData.put("tagId", tagId);
        jdbcTagInsert.execute(eventData);
    }

    private void cleanTagsFromEvent(int eventId) {
        jdbcTemplate.update("DELETE FROM eventtags WHERE eventid = ?", eventId);
    }

    @Override
    public void book(List<Booking> bookings, long userId, long eventId, Locale locale) {
        for (Booking booking : bookings) {
            if (booking.getQty() == null || booking.getQty() <= 0)
                continue;

            jdbcTemplate.update("UPDATE tickets SET booked = booked + ? WHERE eventId = ? AND ticketId = ?", booking.getQty(), eventId, booking.getTicketId());
            int rowsUpdated = jdbcTemplate.update("UPDATE bookings SET qty = qty + ? WHERE eventId = ? AND userId = ? AND ticketId = ?", booking.getQty(), eventId, userId, booking.getTicketId());
            if (rowsUpdated <= 0) {
                final Map<String, Object> bookingData = new HashMap<>();
                bookingData.put("eventId", eventId);
                bookingData.put("userId", userId);
                bookingData.put("qty", booking.getQty());
                bookingData.put("ticketId", booking.getTicketId());
                jdbcBookInsert.execute(bookingData);
            }
        }
        Event event = getEventById(eventId, locale).orElse(null);
        if (event != null && event.getMaxCapacity() <= 0)
            soldOut((int) eventId);
    }

    @Override
    public void cancelBooking(List<Booking> bookings, long userId, long eventId) {
        for (Booking booking : bookings) {
            if (booking.getQty() == null || booking.getQty() <= 0)
                continue;

            jdbcTemplate.update("UPDATE bookings SET qty = qty - ? WHERE eventId = ? AND userId = ? AND ticketId = ?", booking.getQty(), eventId, userId, booking.getTicketId());
            jdbcTemplate.update("UPDATE tickets SET booked = booked - ? WHERE eventId = ? AND ticketid = ?", booking.getQty(), eventId, booking.getTicketId());
        }
    }

    @Override
    public Integer getAttendanceOfEventId(long eventId) {
        return jdbcTemplate.query("SELECT SUM(qty) AS qty FROM bookings WHERE eventid = ?", new Object[]{ eventId },
                (rs, i) -> rs.getInt("qty")).stream().findFirst().orElse(null);
    }

    @Override
    public List<Event> getSimilarEvents(long eventId, Locale locale) {
        String localeExt = JdbcUtils.getLocateExt(locale);
        return jdbcTemplate.query("WITH event_cte AS (SELECT * FROM event_complete" + localeExt + " WHERE eventId = ?) " +
                "SELECT *, (SELECT COUNT(*) FROM (SELECT unnest((SELECT tagIds FROM event_cte)) INTERSECT SELECT unnest(tagIds)) AS aux) AS similarity " +
                "FROM (SELECT * FROM event_complete" + localeExt + " e WHERE e.typeId = (SELECT typeid FROM event_cte) AND e.locationId = (SELECT locationid FROM event_cte) " +
                "AND e.eventid <> (SELECT eventid FROM event_cte)) as eliteTt WHERE state != 1 AND state != 2 AND date > ? ORDER BY similarity DESC LIMIT 5",
                new Object[] { eventId, Timestamp.valueOf(LocalDateTime.now()) }, JdbcUtils.EVENT_ROW_MAPPER);
    }

    @Override
    public List<Event> getPopularEvents(long eventId, Locale locale) {
        return jdbcTemplate.query("SELECT * FROM (SELECT b.eventId, COUNT(b.userId) AS popularity " +
                "FROM bookings b JOIN (SELECT userId FROM bookings WHERE bookings.eventId = ?) AS aux ON b.userId = aux.userId " +
                "WHERE b.eventid <> ? GROUP BY b.eventId ORDER BY popularity DESC) AS aux " +
                "JOIN event_complete" + JdbcUtils.getLocateExt(locale) + " ec on aux.eventid = ec.eventid WHERE state != 1 AND state != 2 AND date > ? LIMIT 5",
                new Object[] { eventId, eventId, Timestamp.valueOf(LocalDateTime.now()) }, JdbcUtils.EVENT_ROW_MAPPER);
    }

    @Override
    public void addTicket(long eventId, String ticketName, double price, int qty) {
        final Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("eventId", eventId);
        bookingData.put("name", ticketName);
        bookingData.put("price", price);
        bookingData.put("booked", 0);
        bookingData.put("maxTickets", qty);
        jdbcTicketInsert.execute(bookingData);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return jdbcTemplate.query("SELECT * FROM tickets WHERE ticketId = ?", new Object[] { ticketId }, JdbcUtils.TICKET_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public void updateTicket(long id, String ticketName, double price, int booked, int qty) {
        jdbcTemplate.update("UPDATE tickets SET name = ?, price = ?, booked = ?, maxTickets = ? WHERE ticketId = ?", ticketName, price, booked, qty, id);
    }

    @Override
    public void deleteTicket(int ticketId) {
        jdbcTemplate.update("DELETE FROM tickets WHERE ticketId = ?", ticketId);
    }
}
