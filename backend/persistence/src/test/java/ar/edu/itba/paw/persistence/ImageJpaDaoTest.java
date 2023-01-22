package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
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
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ImageJpaDao imageDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
    }

    @Test
    public void testGetImageById() {
        TestUtils.IMAGE = TestUtils.createImage(em);
        final Image image = imageDao.getImageById(TestUtils.IMAGE.getId()).orElse(null);
        Assert.assertNotNull(image);
        Assert.assertEquals(Arrays.toString(image.getImage()), Arrays.toString(TestUtils.BYTE_ARRAY));
    }

    @Test
    public void testCreateImage() {
        final Image image = imageDao.createImage(TestUtils.BYTE_ARRAY);
        Assert.assertEquals(1, image.getId());
    }
}