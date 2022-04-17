package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.*;

@Repository
public class TagJdbcDao implements TagDao {
    private final JdbcTemplate jdbcTemplate;

    private final static RowMapper<Tag> ROW_MAPPER = (rs, rowNum) -> new Tag(
            rs.getInt("tagId"),
            rs.getString("name")
    );

    @Autowired
    public TagJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query("SELECT * FROM tags", ROW_MAPPER);
    }

    @Override
    public Optional<Tag> getTagById(long id) {
        List<Tag> query = jdbcTemplate.query("SELECT * FROM tags WHERE tagId = ?", new Object[] {id}, ROW_MAPPER);
        return query.stream().findFirst();
    }
}
