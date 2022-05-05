package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.awt.print.Book;
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
    private final RowMapper<Event> ROW_MAPPER = (rs, i) -> {
        Location location = new Location(rs.getInt("locationId"), rs.getString("locName"));
        Type type = new Type(rs.getInt("typeId"), rs.getString("typeName"));

        return new Event(
                rs.getInt("eventId"),
                rs.getString("name"),
                rs.getString("description"),
                location,
                rs.getInt("ticketsLeft"),
                rs.getDouble("minPrice"),
                type,
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getInt("imageId"),
                Tag.getTags(rs.getArray("tagIds"), rs.getArray("tagNames")),
                new User(rs.getInt("userId"), rs.getString("username")),
                rs.getInt("attendance"),
                State.getState(rs.getInt("state")),
                Ticket.getTickets(rs.getArray("ticketIds"), rs.getArray("ticketNames"), rs.getArray("ticketPrices"), rs.getArray("ticketTicketsLeft"))
        );
    };

    @Autowired
    public EventJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
        jdbcBookInsert = new SimpleJdbcInsert(ds).withTableName("bookings");
        jdbcTagInsert = new SimpleJdbcInsert(ds).withTableName("eventtags");
        jdbcTicketInsert = new SimpleJdbcInsert(ds).withTableName("tickets").usingGeneratedKeyColumns("ticketid");
    }

    @Override
    public Optional<Event> getEventById(long id) {
        return jdbcTemplate.query("SELECT * FROM event_complete WHERE eventId = ?", new Object[]{id}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Event create(String name, String description, int locationId, int ticketsLeft, double price, int typeId,
                        LocalDateTime date, int imageId, Integer[] tagIds, int userId) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("locationId", locationId);
//        eventData.put("ticketsLeft", ticketsLeft);
//        eventData.put("price", price);
        eventData.put("typeId", typeId);
        eventData.put("date", Timestamp.valueOf(date));
        eventData.put("imageId", imageId);
        eventData.put("userId", userId);
        eventData.put("attendance", 0);
        eventData.put("state", State.ACTIVE.ordinal());
        final int eventId = jdbcInsert.executeAndReturnKey(eventData).intValue();
        for (Integer tagId : tagIds)
            addTagToEvent(eventId, tagId);

        return getEventById(eventId).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Event> filterBy(String[] locations, String[] types , String minPrice, String maxPrice,
                                String searchQuery, String order, String orderBy, int page) {
        StringBuilder query = new StringBuilder("SELECT * FROM event_complete WHERE state != 1");
        if (locations != null || minPrice != null || maxPrice != null || types != null || searchQuery != null) {
            if (locations != null && locations.length > 0) {
                String lastLocation = locations[locations.length - 1];
                query.append("AND locationid IN (");
                for (String location : locations) {
                    query.append("'").append(location).append("'");
                    if (!Objects.equals(location, lastLocation))
                        query.append(", ");
                }
                query.append(")");
            }
            if (minPrice != null) {
                query.append(" AND price >= ").append(minPrice);
            }
            if (maxPrice != null) {
                query.append(" AND price <= ").append(maxPrice);
            }
            if (types != null && types.length > 0) {
                String lastType = types[types.length - 1];
                query.append(" AND typeid IN (");
                for (String type : types) {
                    query.append("'").append(type).append("'");
                    if (!Objects.equals(type, lastType))
                        query.append(", ");
                }
                query.append(")");
            }
//            if (tags != null) {
//                String lastTag = tags[tags.length - 1];
//                query.append(" AND (SELECT COUNT(*) FROM (SELECT aux FROM UNNEST('{");
//                for (String tag : tags) {
//                    query.append(tag);
//                    if (!Objects.equals(tag, lastTag))
//                        query.append(", ");
//                }
//                query.append("}'::INTEGER[]) AS aux WHERE aux <> ALL tagIds) = 0 ");
//            }
            if (searchQuery != null) {
                query.append(" AND ((SELECT to_tsvector('Spanish', name) @@ to_tsquery('");
                query.append(searchQuery).append("')) = 't' OR name ILIKE '%").append(searchQuery).append("%')");
            }
        }

        if (order != null && orderBy != null) {
            query.append(" ORDER BY ").append(order).append(" ").append(orderBy);
        }

        return jdbcTemplate.query(query + "  LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public List<Event> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM event_complete LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public List<Event> getFewTicketsEvents() {
        return jdbcTemplate.query("SELECT * FROM event_complete WHERE attendance >= (4 * ticketsLeft) AND state != 1 AND ticketsLeft > 0 LIMIT 4", ROW_MAPPER);
    }

    @Override
    public List<Event> getUpcomingEvents(){
        return jdbcTemplate.query("SELECT * FROM event_complete WHERE date > ? AND state != 1 ORDER BY date LIMIT 5",
                new Object[]{Timestamp.valueOf(LocalDateTime.now())}, ROW_MAPPER);
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
    public List<Event> getUserEvents(long id, int page) {
        return jdbcTemplate.query("SELECT * FROM event_complete WHERE userid = ? LIMIT 10 OFFSET ?", new Object[]{id, (page - 1) * 10}, ROW_MAPPER);
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
    public boolean book(List<Booking> bookings, long userId, long eventId) {
        for (Booking booking : bookings) {
            jdbcTemplate.update("UPDATE events SET attendance = attendance + ? WHERE eventId = ?", booking.getQty(), eventId);
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
        return true;
    }

    @Override
    public boolean cancelBooking(long userId, long eventId, int qty, long ticketId) {
        int rowsUpdated = jdbcTemplate.update("UPDATE bookings SET qty = qty - ? WHERE eventId = ? AND userId = ? AND ticketId = ?", qty, eventId, userId, ticketId);
        if (rowsUpdated <= 0)
            return false;
        rowsUpdated = jdbcTemplate.update("UPDATE events SET attendance = attendance - ? WHERE eventId = ?", qty, eventId);
        return rowsUpdated > 0;
    }

    @Override
    public Integer getAttendanceOfEventId(long eventId) {
        return jdbcTemplate.query("SELECT SUM(qty) AS qty FROM bookings WHERE eventid = ?", new Object[]{ eventId },
                (rs, i) -> rs.getInt("qty")).stream().findFirst().orElse(null);
    }

    @Override
    public List<Event> getSimilarEvents(long eventId) {
        return jdbcTemplate.query("WITH event_cte AS (SELECT * FROM event_complete WHERE eventId = ?) " +
                "SELECT *, (SELECT COUNT(*) FROM (SELECT unnest((SELECT tagIds FROM event_cte)) INTERSECT SELECT unnest(tagIds)) AS aux) AS similarity " +
                "FROM (SELECT * FROM event_complete e " +
                "WHERE e.typeId = (SELECT typeid FROM event_cte) AND e.locationId = (SELECT locationid FROM event_cte) AND e.eventid <> (SELECT eventid FROM event_cte)) as eliteTt " +
                "ORDER BY similarity DESC LIMIT 5", new Object[] { eventId }, ROW_MAPPER);
    }

    @Override
    public List<Event> getPopularEvents(long eventId) {
        return jdbcTemplate.query("SELECT * FROM (SELECT b.eventId, COUNT(b.userId) AS popularity " +
                "FROM bookings b JOIN (SELECT userId FROM bookings WHERE bookings.eventId = ?) AS aux ON b.userId = aux.userId " +
                "WHERE b.eventid <> ? GROUP BY b.eventId ORDER BY popularity DESC) AS aux " +
                "JOIN event_complete ec on aux.eventid = ec.eventid LIMIT 5", new Object[] { eventId, eventId }, ROW_MAPPER);
    }

    @Override
    public void addTicket(long eventId, Ticket ticket) {
        final Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("eventId", eventId);
        bookingData.put("name", ticket.getTicketName());
        bookingData.put("price", ticket.getPrice());
        bookingData.put("booked", ticket.getQty());
        bookingData.put("ticketsLeft", ticket.getQty());
        jdbcTicketInsert.execute(bookingData);
    }
}
