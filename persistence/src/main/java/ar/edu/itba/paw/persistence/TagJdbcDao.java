package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.*;

@Repository
public class TagJdbcDao implements TagDao {
    private final JdbcTemplate jdbcTemplate;

    public final static RowMapper<Tag> ROW_MAPPER = (rs, rowNum) -> new Tag(
            rs.getInt("tagId"),
            rs.getString("name")
    );

    @Autowired
    public TagJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Tag> getAll(Locale locale) {
        return jdbcTemplate.query("SELECT * FROM tags" + JdbcUtils.getLocateExt(locale)  + " ORDER BY name", ROW_MAPPER);
    }
}
