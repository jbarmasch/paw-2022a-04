package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Type;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class TypeDto {
    private long id;
    private String name;

    private URI self;

    public static TypeDto fromType(final UriInfo uriInfo, final Type type) {
        final TypeDto dto = new TypeDto();

        dto.id = type.getId();
        dto.name = type.getName();

        final UriBuilder locationUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("locations").path(String.valueOf(type.getId()));
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
}
