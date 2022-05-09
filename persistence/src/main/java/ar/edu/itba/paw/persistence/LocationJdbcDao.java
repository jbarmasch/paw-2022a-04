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
    public final static RowMapper<Location> ROW_MAPPER = (rs, rowNum) -> new Location(
            rs.getInt("locationId"),
            rs.getString("name")
    );

    @Autowired
    public LocationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Location> getAll() {
        return jdbcTemplate.query("SELECT * FROM locations ORDER BY name", ROW_MAPPER);
    }
}
