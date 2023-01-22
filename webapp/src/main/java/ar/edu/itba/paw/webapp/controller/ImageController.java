package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.EventList;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("api/image")
@Component
public class ImageController {
    @Autowired
    private ImageService is;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        Optional<ImageDto> imageDto = is.getImageById(id).map(i -> ImageDto.fromImage(uriInfo, i));

        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(86400);
        cacheControl.setPrivate(false);
        cacheControl.setMustRevalidate(true);

        if (imageDto.isPresent()) {
            return Response.ok(imageDto.get()).cacheControl(cacheControl).build();
        } else {
            LOGGER.error("Image not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
