package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TypeJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TypeJpaDao typeDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
        TestUtils.createType(em);
        TestUtils.createType(em);
    }

    @Test
    public void testGetAllTypesEnglish() {
        List<Type> types = typeDao.getAll();
        Assert.assertNotNull(types);
        Assert.assertEquals(2, types.size());
    }

    @Test
    public void testGetAllEnglishIntegrity() {
        Type.setLocale(new Locale("en"));
        List<Type> types = typeDao.getAll();
        Assert.assertEquals(TestUtils.TYPE_NAME_EN, types.get(0).getName());
    }

    @Test
    public void testGetAllSpanishIntegrity() {
        Type.setLocale(new Locale("es"));
        List<Type> types = typeDao.getAll();
        Assert.assertEquals(TestUtils.TYPE_NAME, types.get(0).getName());

    }

    @Test
    public void testGetAllDefaultIntegrity() {
        Type.setLocale(new Locale("da"));
        List<Type> types = typeDao.getAll();
        Assert.assertEquals(TestUtils.TYPE_NAME_EN, types.get(0).getName());
    }
}