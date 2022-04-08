package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            rs.getDouble("price")
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
    public Event create(String name, String description, String location, int maxCapacity, double price) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("location", location);
        eventData.put("maxCapacity", maxCapacity);
        eventData.put("price", price);

        final Number eventId = jdbcInsert.executeAndReturnKey(eventData);
        return new Event(eventId.intValue(), name, description, location, maxCapacity, price);
    }

    public List<Event> filterByLocation(List<String> locations, int page) {
        StringBuilder aux = new StringBuilder();
        for (String location : locations) {
            aux.append(", ").append(location);
        }
        return jdbcTemplate.query("SELECT * FROM events WHERE location IN [?] LIMIT 10 OFFSET ?", new Object[]{aux ,(page - 1) * 10}, ROW_MAPPER);
    }

    public List<Event> filterByPrice(Double minPrice, Double maxPrice, int page) {
        StringBuilder query = new StringBuilder("SELECT * FROM events WHERE ");
        if (minPrice != null) {
            query.append("price > ").append(minPrice);
            if (maxPrice != null)
                query.append("AND ");
        }
        if (maxPrice != null)
            query.append("price < ").append(maxPrice);
        query.append("LIMIT 10 OFFSET ?");
        return jdbcTemplate.query(query.toString(), new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public List<Event> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM events LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }
}
