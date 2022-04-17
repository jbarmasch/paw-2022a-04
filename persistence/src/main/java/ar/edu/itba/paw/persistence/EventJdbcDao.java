package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class EventJdbcDao implements EventDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Event> ROW_MAPPER = (rs, rowNum) -> new Event(
            rs.getInt("eventId"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getInt("locationId"),
            rs.getInt("maxCapacity"),
            rs.getDouble("price"),
            rs.getInt("typeId"),
            rs.getTimestamp("date")
    );

    @Autowired
    public EventJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
    }

    @Override
    public Optional<Event> getEventById(long id) {
        List<Event> query = jdbcTemplate.query("SELECT * FROM events WHERE eventid = ?", new Object[]{id}, ROW_MAPPER);
        return query.stream().findFirst();
    }

    @Override
    public Event create(String name, String description, Integer locationId, int maxCapacity, double price, int typeId, Timestamp date) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("locationId", locationId);
        eventData.put("maxCapacity", maxCapacity);
        eventData.put("price", price);
        eventData.put("typeId", typeId);
        eventData.put("date", date);

        final Number eventId = jdbcInsert.executeAndReturnKey(eventData);
        return new Event(eventId.intValue(), name, description, locationId, maxCapacity, price, typeId, date);
    }

    public List<Event> filterBy(Integer[] locations, String[] types , Double minPrice, Double maxPrice, int page) {
        StringBuilder query = new StringBuilder("SELECT * FROM events");

        if (locations != null || minPrice != null || maxPrice != null || types != null) {
            boolean append = false;
            query.append(" WHERE ");
            if (locations != null && locations.length > 0) {
                append = true;
                Integer lastLocation = locations[locations.length - 1];
                query.append("locationId IN (");
                for (Integer location : locations) {
                    query.append("'").append(location).append("'");
                    if (!Objects.equals(location, lastLocation))
                        query.append(", ");
                }
                query.append(")");
            }
            if (minPrice != null) {
                if (append)
                    query.append(" AND ");
                append = true;
                query.append("price >= ").append(minPrice);
            }
            if (maxPrice != null) {
                if (append)
                    query.append(" AND ");
                append = true;
                query.append("price <= ").append(maxPrice);
            }
            if (types != null && types.length > 0) {
                if (append)
                    query.append(" AND ");
                String lastType = types[types.length - 1];
                query.append("typeId IN (");
                for (String type : types) {
                    query.append("'").append(type).append("'");
                    if (!Objects.equals(type, lastType))
                        query.append(", ");
                }
                query.append(")");
            }
        }

        return jdbcTemplate.query(query + " LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public List<Event> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM events LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int type, Timestamp date) {
        jdbcTemplate.update("UPDATE events SET name = ?, description = ?, locationid = ?, maxcapacity = ?, price = ?, type = ?, date = ? WHERE eventid = ?",
                name, description, locationId, maxCapacity, price, type, date, id);
    }

    @Override
    public void deleteEvent(int id) {
        jdbcTemplate.update("DELETE FROM events WHERE eventId = ?", id);
    }
}
