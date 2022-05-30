package ar.edu.itba.paw.persistence;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class LocationJdbcDaoTest {
//    private static final Location LOCATION1 = new Location(1, "Location1");
//    private static final Location LOCATION2 = new Location(2, "Location2");
//    private static final Location LOCATION3 = new Location(3, "Location3");
//
//    @Autowired
//    private DataSource ds;
//    @Autowired
//    private LocationJdbcDao locationDao;
//
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsert;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("locations").usingGeneratedKeyColumns("locationid");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "locations");
//
//        Map<String, Object> location = new HashMap<>();
//        location.put("name", LOCATION1.getName());
//        jdbcInsert.execute(location);
//        location.put("name", LOCATION2.getName());
//        jdbcInsert.execute(location);
//        location.put("name", LOCATION3.getName());
//        jdbcInsert.execute(location);
//    }
//
//    @Test
//    public void testGetAllLocations() {
//        List<Location> locations = locationDao.getAll();
//
//        Assert.assertNotNull(locations);
//        Assert.assertEquals(3, locations.size());
//        Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "locations"));
//    }
//
//    @Test
//    public void testGetAllIntegrity() {
//        List<Location> locations = locationDao.getAll();
//
//        Assert.assertEquals(LOCATION1.getName(), locations.get(0).getName());
//        Assert.assertEquals(LOCATION2.getName(), locations.get(1).getName());
//        Assert.assertEquals(LOCATION3.getName(), locations.get(2).getName());
//    }
//}