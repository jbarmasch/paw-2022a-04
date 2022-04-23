package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageJdbcDao implements ImageDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    public final static RowMapper<Image> ROW_MAPPER = (rs, rowNum) -> new Image(
            rs.getInt("imageId"),
            rs.getBytes("image")
    );

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("images").usingGeneratedKeyColumns("imageid");
    }

    @Override
    public Optional<Image> getImageById(long id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE imageid = ?", new Object[]{id}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public int addEventImage(byte[] image) {
        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("image", image);

        final Number imageId = jdbcInsert.executeAndReturnKey(imageData);
        return imageId.intValue();
    }

    @Override
    public Optional<Image> getImgFromEventId(int eventId) {
        return jdbcTemplate.query("SELECT images.imageid, images.image FROM events JOIN images ON events.imageid = images.imageid WHERE eventid = ?", new Object[]{eventId}, ROW_MAPPER).stream().findFirst();
    }
}
