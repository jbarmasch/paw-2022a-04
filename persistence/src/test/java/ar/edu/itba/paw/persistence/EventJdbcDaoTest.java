package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EventJdbcDaoTest {
    private static final byte[] BYTE_ARRAY = new byte[] {0x00};

    @Autowired
    private DataSource ds;

    @Autowired
    private EventJdbcDao eventDao;
    @Autowired
    private UserJdbcDao userDao;
    @Autowired
    private ImageJdbcDao imageDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertLocation;
    private SimpleJdbcInsert jdbcInsertType;
    private SimpleJdbcInsert jdbcInsertTag;
    private SimpleJdbcInsert jdbcInsertImage;
    private SimpleJdbcInsert jdbcInsertRole;

    private final String GENERIC = "genericName";
    private final String EVENTNAME = "eventName";
    private final String EVENTDESC = "eventDesc";
    private final String USERNAME = "user";
    private final LocalDateTime EVENTDATE = LocalDateTime.of(3025, Month.JULY, 29, 19, 30, 40);

    @Before
    public void setUp() {
        System.out.println("hola");
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
        jdbcInsertLocation = new SimpleJdbcInsert(ds).withTableName("locations").usingGeneratedKeyColumns("locationid");
        jdbcInsertType = new SimpleJdbcInsert(ds).withTableName("types").usingGeneratedKeyColumns("typeid");
        jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName("tags").usingGeneratedKeyColumns("tagid");
        jdbcInsertImage = new SimpleJdbcInsert(ds).withTableName("images").usingGeneratedKeyColumns("imageid");
        jdbcInsertRole = new SimpleJdbcInsert(ds).withTableName("roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "events");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "locations");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "types");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tags");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "eventtags");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "userroles");
        jdbcTemplate.query("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK", r -> r);

        Map<String, Object> generic = new HashMap<>();
        generic.put("name", GENERIC);
        jdbcInsertLocation.executeAndReturnKey(generic);
        jdbcInsertType.execute(generic);
        jdbcInsertTag.execute(generic);

        imageDao.addEventImage(BYTE_ARRAY);

        Map<String, Object> userRole = new HashMap<>();
        userRole.put("roleid", 1);
        userRole.put("name", "ROLE_USER");
        jdbcInsertRole.execute(userRole);
        userRole = new HashMap<>();
        userRole.put("roleid", 2);
        userRole.put("name", "ROLE_CREATOR");
        jdbcInsertRole.execute(userRole);

        userDao.create(USERNAME, "useruser", "user@user.com");
    }

    @Test
    public void testCreateEvent() {
        final Event event = eventDao.create(EVENTNAME, EVENTDESC, 0, 0, EVENTDATE, 0, new Integer[] {0}, 0, new Locale("es"));
        Assert.assertNotNull(event);
        Assert.assertEquals(EVENTNAME, event.getName());
        Assert.assertEquals(EVENTDESC, event.getDescription());
        Assert.assertEquals(EVENTDATE, event.getDate());
        Assert.assertEquals(GENERIC, event.getLocation().getName());
        Assert.assertEquals(GENERIC, event.getType().getName());
        Assert.assertEquals(GENERIC, event.getTags().get(0).getName());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events"));
    }

    @Test
    public void testFilterBy() {
        final Event event = eventDao.create(EVENTNAME, EVENTDESC, 0, 0, EVENTDATE, 0, new Integer[] {0}, 0, new Locale("es"));
//        List<Event> events = eventDao.filterBy(new String[] {"0"}, new String[] {"0"}, null, null, null, null, USERNAME, null, null, 1, new Locale("es"));
        List<Event> events = eventDao.filterBy(null, null, null, null, null, new String[] {"0"}, null, null, null, 1, new Locale("es"));
        Assert.assertNotNull(events);
        Assert.assertNotNull(events.get(0));
        Assert.assertEquals(event.getName(), events.get(0).getName());

//        events = eventDao.filterBy(new String[] {"0"}, new String[] {"0"}, null, null, null, null, USERNAME, null, null, 1, new Locale("es"));
//        Assert.assertNotNull(events);
//        Assert.assertNull(events.get(0));
    }
}