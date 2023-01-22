package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;
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
public class TagJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TagJpaDao tagDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
        TestUtils.createTag(em);
        TestUtils.createTag(em);
    }

    @Test
    public void testGetAllTagsEnglish() {
        List<Tag> tags = tagDao.getAll();
        Assert.assertNotNull(tags);
        Assert.assertEquals(2, tags.size());
    }

    @Test
    public void testGetAllEnglishIntegrity() {
        Tag.setLocale(new Locale("en"));
        List<Tag> tags = tagDao.getAll();
        Assert.assertEquals(TestUtils.TYPE_NAME_EN, tags.get(0).getName());
    }

    @Test
    public void testGetAllSpanishIntegrity() {
        Tag.setLocale(new Locale("es"));
        List<Tag> tags = tagDao.getAll();
        Assert.assertEquals(TestUtils.TYPE_NAME, tags.get(0).getName());

    }

    @Test
    public void testGetAllDefaultIntegrity() {
        Tag.setLocale(new Locale("da"));
        List<Tag> tags = tagDao.getAll();
        Assert.assertEquals(TestUtils.TYPE_NAME_EN, tags.get(0).getName());
    }
}