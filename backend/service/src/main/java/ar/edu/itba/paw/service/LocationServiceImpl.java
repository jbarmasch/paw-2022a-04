package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.persistence.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationDao locationDao;

    @Override
    public List<Location> getAll() {
        return locationDao.getAll();
    }
}
