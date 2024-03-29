package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Location;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class LocationDto {
    private long id;
    private String name;

    private URI self;

    public static LocationDto fromLocation(final UriInfo uriInfo, final Location location) {
        final LocationDto dto = new LocationDto();

        dto.id = location.getId();
        dto.name = location.getName();

        final UriBuilder locationUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/locations").path(String.valueOf(location.getId()));
        dto.self = locationUriBuilder.build();

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    @Override
    public int hashCode() {
        return ((int) id);
    }
}
