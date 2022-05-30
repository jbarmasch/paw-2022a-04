package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class ImageJdbcDaoTest {
//    private static final byte[] BYTE_ARRAY = new byte[] {0x00};
//
//    @Autowired
//    private DataSource ds;
//    @Autowired
//    private ImageJdbcDao imageDao;
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
//        jdbcTemplate.query("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK", r -> r);
//    }
//
//    @Test
//    public void testGetImageById() {
//        final int imageId = imageDao.addEventImage(BYTE_ARRAY);
//        final Image image = imageDao.getImageById(imageId).orElse(null);
//        Assert.assertNotNull(image);
//        Assert.assertEquals(Arrays.toString(image.getImage()), Arrays.toString(BYTE_ARRAY));
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"));
//    }
//
//    @Test
//    public void testAddImage() {
//        final int imageId = imageDao.addEventImage(BYTE_ARRAY);
//        Assert.assertEquals(0, imageId);
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"));
//    }
//}