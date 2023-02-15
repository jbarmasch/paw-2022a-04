package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Tag;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class TagDto {
    private long id;
    private String name;
    private String name_en;

    private URI self;

    public static TagDto fromTag(final UriInfo uriInfo, final Tag tag) {
        final TagDto dto = new TagDto();

        dto.id = tag.getId();
        dto.name = tag.getName();

        final UriBuilder locationUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/tags").path(String.valueOf(tag.getId()));
        dto.self = locationUriBuilder.build();

        return dto;
    }

    public static TagDto fromTag(final UriInfo uriInfo, final Tag tag, final String locale) {
        final TagDto dto = new TagDto();

        dto.id = tag.getId();
        dto.name = tag.getName(locale);

        final UriBuilder locationUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/tags").path(String.valueOf(tag.getId()));
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
