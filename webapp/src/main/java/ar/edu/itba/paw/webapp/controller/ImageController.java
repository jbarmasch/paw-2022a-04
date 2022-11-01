package ar.edu.itba.paw.webapp.controller;

//@Controller
//public class ImageController {
//    @Autowired
//    private ImageService imageService;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
//
//    @RequestMapping(value = "/image/{id}", method = { RequestMethod.GET }, produces = MediaType.IMAGE_PNG_VALUE)
//    @ResponseBody
//    public byte[] getImage(@PathVariable("id") @Min(1) final int imageId) {
//        Image image = imageService.getImageById(imageId).orElse(null);
//        if (image == null) {
//            LOGGER.error("Image not found");
//            throw new ImageNotFoundException();
//        }
//
//        return image.getImage();
//    }
//}

import ar.edu.itba.paw.model.EventList;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("image")
@Component
public class ImageController {
    @Autowired
    private ImageService is;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        Optional<ImageDto> imageDto = is.getImageById(id).map(i -> ImageDto.fromImage(uriInfo, i));

        if (imageDto.isPresent()) {
            return Response.ok(imageDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
