package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;

import java.util.List;

public interface LocationDao {
    List<Location> getAll();
}
