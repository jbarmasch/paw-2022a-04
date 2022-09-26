package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class LocationJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private LocationJpaDao locationDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
        TestUtils.createLocation(em);
        TestUtils.createLocation(em);
    }

    @Test
    public void testGetAllLocations() {
        List<Location> locations = locationDao.getAll();
        Assert.assertNotNull(locations);
        Assert.assertEquals(2, locations.size());
    }
}