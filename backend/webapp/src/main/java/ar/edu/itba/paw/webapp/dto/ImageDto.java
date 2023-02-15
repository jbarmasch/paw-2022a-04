package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Image;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ImageDto {
    private byte[] image;
    private URI self;

    public static ImageDto fromImage(final UriInfo uriInfo, final Image image) {
        final ImageDto dto = new ImageDto();

        dto.image = image.getImage();

        final UriBuilder eventUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/image").path(String.valueOf(image.getId()));
        dto.self = eventUriBuilder.build();

        return dto;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
