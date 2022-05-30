package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Type;
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
import java.util.Locale;
import java.util.Map;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class TypeJdbcDaoTest {
//    private static final Type TYPE1 = new Type(1, "Type1");
//    private static final Type TYPE2 = new Type(2, "Type2");
//    private static final Type TYPE3 = new Type(3, "Type3");
//
//    private static final Type TYPE1_EN = new Type(1, "Type1_EN");
//    private static final Type TYPE2_EN = new Type(2, "Type2_EN");
//    private static final Type TYPE3_EN = new Type(3, "Type3_EN");
//
//    @Autowired
//    private DataSource ds;
//
//    @Autowired
//    private TypeJdbcDao typeDao;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsert;
//    private SimpleJdbcInsert jdbcInsertEn;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("types").usingGeneratedKeyColumns("typeid");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "types");
//        jdbcInsertEn = new SimpleJdbcInsert(ds).withTableName("types_en").usingGeneratedKeyColumns("typeid");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "types_en");
//
//        Map<String, Object> type = new HashMap<>();
//        type.put("name", TYPE1.getName());
//        jdbcInsert.execute(type);
//        type.put("name", TYPE2.getName());
//        jdbcInsert.execute(type);
//        type.put("name", TYPE3.getName());
//        jdbcInsert.execute(type);
//
//        type.put("name", TYPE1_EN.getName());
//        jdbcInsertEn.execute(type);
//        type.put("name", TYPE2_EN.getName());
//        jdbcInsertEn.execute(type);
//        type.put("name", TYPE3_EN.getName());
//        jdbcInsertEn.execute(type);
//    }
//
//    @Test
//    public void testGetAllTypesEnglish() {
//        List<Type> types = typeDao.getAll(new Locale("en"));
//
//        Assert.assertNotNull(types);
//        Assert.assertEquals(3, types.size());
//        Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "types_en"));
//    }
//
//    @Test
//    public void testGetAllEnglishIntegrity() {
//        List<Type> types = typeDao.getAll(new Locale("en"));
//
//        Assert.assertEquals(TYPE1_EN.getName(), types.get(0).getName());
//        Assert.assertEquals(TYPE2_EN.getName(), types.get(1).getName());
//        Assert.assertEquals(TYPE3_EN.getName(), types.get(2).getName());
//    }
//
//    @Test
//    public void typesGetAllSpanish() {
//        List<Type> types = typeDao.getAll(new Locale("es"));
//
//        Assert.assertNotNull(types);
//        Assert.assertEquals(3, types.size());
//        Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "types"));
//    }
//
//    @Test
//    public void testGetAllSpanishIntegrity() {
//        List<Type> types = typeDao.getAll(new Locale("es"));
//
//        Assert.assertEquals(TYPE1.getName(), types.get(0).getName());
//        Assert.assertEquals(TYPE2.getName(), types.get(1).getName());
//        Assert.assertEquals(TYPE3.getName(), types.get(2).getName());
//    }
//
//    @Test
//    public void typesGetAllDefault() {
//        List<Type> types = typeDao.getAll(new Locale("da"));
//
//        Assert.assertNotNull(types);
//        Assert.assertEquals(3, types.size());
//        Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "types"));
//    }
//
//    @Test
//    public void testGetAllDefaultIntegrity() {
//        List<Type> types = typeDao.getAll(new Locale("da"));
//
//        Assert.assertEquals(TYPE1_EN.getName(), types.get(0).getName());
//        Assert.assertEquals(TYPE2_EN.getName(), types.get(1).getName());
//        Assert.assertEquals(TYPE3_EN.getName(), types.get(2).getName());
//    }
//}