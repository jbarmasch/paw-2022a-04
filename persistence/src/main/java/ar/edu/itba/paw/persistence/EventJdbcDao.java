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
            rs.getString("location"),
            rs.getInt("maxCapacity"),
            rs.getDouble("price"),
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
    public Event create(String name, String description, String location, int maxCapacity, double price, Timestamp date) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("location", location);
        eventData.put("maxCapacity", maxCapacity);
        eventData.put("price", price);
        eventData.put("date", date);

        final Number eventId = jdbcInsert.executeAndReturnKey(eventData);
        return new Event(eventId.intValue(), name, description, location, maxCapacity, price, date);
    }

    public List<Event> filterBy(String[] filters, String[] locations, Double minPrice, Double maxPrice, int page) {
        int size = filters.length;
        String lastElement = null;
        if (size > 0)
            lastElement = filters[size - 1];
        StringBuilder query = new StringBuilder("SELECT * FROM events WHERE ");
        for (String filter : filters) {
            switch (filter) {
                case "location":
                    String lastLocation = null;
                    if (locations.length > 0)
                        lastLocation = locations[locations.length - 1];
                    query.append("location IN (");
                    for (String location : locations) {
                        query.append("'").append(location).append("'");
                        if (!Objects.equals(location, lastLocation))
                            query.append(", ");
                    }
                    query.append(")");
                    break;
                case "price":
                    if (minPrice != null) {
                        query.append("price >= ").append(minPrice);
                        if (maxPrice != null)
                            query.append(" AND ");
                    }
                    if (maxPrice != null)
                        query.append("price <= ").append(maxPrice);
                    break;
            }
            if (!filter.equals(lastElement))
                query.append(" AND ");
        }

        return jdbcTemplate.query(query + " LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public List<Event> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM events LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }
}
