package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
import java.util.List;
import java.util.Optional;

public interface LocationDao {
    List<Location> getAll();
    Optional<Location> getLocationById(long id);
    Optional<Location> getLocationFromEventId(int eventId);
}
