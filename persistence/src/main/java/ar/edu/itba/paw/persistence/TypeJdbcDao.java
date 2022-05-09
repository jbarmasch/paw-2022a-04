package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.*;

@Repository
public class TypeJdbcDao implements TypeDao {
    private final JdbcTemplate jdbcTemplate;
    public final static RowMapper<Type> ROW_MAPPER = (rs, rowNum) -> new Type(
            rs.getInt("typeId"),
            rs.getString("name")
    );

    @Autowired
    public TypeJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Type> getAll(Locale locale) {
        return jdbcTemplate.query("SELECT * FROM types" + JdbcUtils.getLocateExt(locale) + " ORDER BY name", ROW_MAPPER);
    }
}
