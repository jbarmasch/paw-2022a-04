package ar.edu.itba.paw.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EventTagJdbcDao implements EventTagDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public EventTagJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("eventtags");
    }

    @Override
    public void addTagToEvent(int eventId, int tagId) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventId", eventId);
        eventData.put("tagId", tagId);

        jdbcInsert.execute(eventData);
    }

    @Override
    public void cleanTagsFromEvent(int eventId) {
        jdbcTemplate.update("DELETE FROM eventtags WHERE eventid = ?", eventId);
    }
}
