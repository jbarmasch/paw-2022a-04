package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("api/image")
@Component
public class ImageController {
    @Autowired
    private ImageService is;
    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @Produces({"image/png", "image/jpeg", "image/jpg"})
    public Response getById(@PathParam("id") final long id) {
        Image image = is
                .getImageById(id)
                .orElseThrow(ImageNotFoundException::new);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(86400);
        cacheControl.setPrivate(false);
        cacheControl.setMustRevalidate(true);

        return Response.ok(image.getImage()).cacheControl(cacheControl).type("image/png").build();
    }
}
