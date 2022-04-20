package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.*;

@Repository
public class LocationJdbcDao implements LocationDao {
    private final JdbcTemplate jdbcTemplate;

    private final static RowMapper<Location> ROW_MAPPER = (rs, rowNum) -> new Location(
            rs.getInt("locationId"),
            rs.getString("name")
    );

    @Autowired
    public LocationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Location> getAll() {
        return jdbcTemplate.query("SELECT * FROM locations", ROW_MAPPER);
    }

    @Override
    public Optional<Location> getLocationById(long id) {
        List<Location> query = jdbcTemplate.query("SELECT * FROM locations WHERE locationId = ?", new Object[] {id}, ROW_MAPPER);
        return query.stream().findFirst();
    }

    @Override
    public Optional<Location> getLocationFromEventId(int eventId) {
        return jdbcTemplate.query("SELECT locations.locationid, locations.name FROM events JOIN locations ON events.locationid = locations.locationid WHERE eventid = ?", new Object[]{eventId}, ROW_MAPPER).stream().findFirst();
    }
}
