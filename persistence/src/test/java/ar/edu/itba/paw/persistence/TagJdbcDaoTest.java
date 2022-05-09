package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TagJdbcDaoTest {
    private static final Tag TAG1 = new Tag(1, "Tag1");
    private static final Tag TAG2 = new Tag(2, "Tag2");
    private static final Tag TAG3 = new Tag(3, "Tag3");
    private static final Tag TAG1_EN = new Tag(1, "Tag1_EN");
    private static final Tag TAG2_EN = new Tag(2, "Tag2_EN");
    private static final Tag TAG3_EN = new Tag(3, "Tag3_EN");

    @Autowired
    private DataSource ds;
    @Autowired
    private TagJdbcDao tagDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertEn;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("tags").usingGeneratedKeyColumns("tagid");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tags");
        jdbcInsertEn = new SimpleJdbcInsert(ds).withTableName("tags_en").usingGeneratedKeyColumns("tagid");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tags_en");

        Map<String, Object> tag = new HashMap<>();
        tag.put("name", TAG1.getName());
        jdbcInsert.execute(tag);
        tag.put("name", TAG2.getName());
        jdbcInsert.execute(tag);
        tag.put("name", TAG3.getName());
        jdbcInsert.execute(tag);

        tag.put("name", TAG1_EN.getName());
        jdbcInsertEn.execute(tag);
        tag.put("name", TAG2_EN.getName());
        jdbcInsertEn.execute(tag);
        tag.put("name", TAG3_EN.getName());
        jdbcInsertEn.execute(tag);
    }

    @Test
    public void testGetAllTagsEnglish() {
        List<Tag> tags = tagDao.getAll(new Locale("en"));

        Assert.assertNotNull(tags);
        Assert.assertEquals(3, tags.size());
        Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "tags_en"));
    }

    @Test
    public void testGetAllEnglishIntegrity() {
        List<Tag> tags = tagDao.getAll(new Locale("en"));

        Assert.assertEquals(TAG1_EN.getName(), tags.get(0).getName());
        Assert.assertEquals(TAG2_EN.getName(), tags.get(1).getName());
        Assert.assertEquals(TAG3_EN.getName(), tags.get(2).getName());
    }

    @Test
    public void tagsGetAllSpanish() {
        List<Tag> tags = tagDao.getAll(new Locale("es"));

        Assert.assertNotNull(tags);
        Assert.assertEquals(3, tags.size());
        Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "tags"));
    }

    @Test
    public void testGetAllSpanishIntegrity() {
        List<Tag> tags = tagDao.getAll(new Locale("es"));

        Assert.assertEquals(TAG1.getName(), tags.get(0).getName());
        Assert.assertEquals(TAG2.getName(), tags.get(1).getName());
        Assert.assertEquals(TAG3.getName(), tags.get(2).getName());
    }

    @Test
    public void tagsGetAllDefault() {
        List<Tag> tags = tagDao.getAll(new Locale("da"));

        Assert.assertNotNull(tags);
        Assert.assertEquals(3, tags.size());
        Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "tags"));
    }

    @Test
    public void testGetAllDefaultIntegrity() {
        List<Tag> tags = tagDao.getAll(new Locale("da"));

        Assert.assertEquals(TAG1_EN.getName(), tags.get(0).getName());
        Assert.assertEquals(TAG2_EN.getName(), tags.get(1).getName());
        Assert.assertEquals(TAG3_EN.getName(), tags.get(2).getName());
    }
}