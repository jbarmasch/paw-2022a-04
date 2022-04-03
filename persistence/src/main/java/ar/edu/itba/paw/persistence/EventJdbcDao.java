package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Location;
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
            rs.getObject("location", Location.class),
            rs.getInt("maxCapacity"),
            rs.getInt("attendance")
    );

    @Autowired
    public EventJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventId");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS events ("
                + "eventId SERIAL PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "description VARCHAR(100) NOT NULL,"
                + "location VARCHAR(100),"
                + "maxCapacity INTEGER NOT NULL"
                + "attendance INTEGER NOT NULL"
                + "CHECK (location IN ('Adrogué', 'San Isidro', 'Don Torcuato', 'Marimashita Iruma Kun', 'Belgrano', 'Recoleta', 'Turdera'))"
                + ")");
    }

    @Override
    public Optional<Event> getEventById(long id) {
        List<Event> query = jdbcTemplate.query("SELECT * FROM events WHERE eventId = ?", new Object[]{id}, ROW_MAPPER);
        return query.stream().findFirst();
    }

    @Override
    public Event create(String name, String description, Location location, int maxCapacity, int attendance) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("location", location);
        eventData.put("maxCapacity", maxCapacity);
        eventData.put("attendance", attendance);

        final int eventId = jdbcInsert.execute(eventData);
        return new Event(eventId, name, description, location, maxCapacity, attendance);
    }

    public List<Event> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM events LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }
}
