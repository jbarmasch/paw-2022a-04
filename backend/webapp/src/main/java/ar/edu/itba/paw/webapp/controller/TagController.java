package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.service.TagService;
import ar.edu.itba.paw.webapp.dto.TagDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("api/tags")
@Component
public class TagController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagController.class);
    @Autowired
    private TagService ts;
    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listTags() {
        final List<Tag> res = ts.getAll();
        final List<TagDto> tagList = res
                .stream()
                .map(e -> TagDto.fromTag(uriInfo, e, request.getLocale().getLanguage()))
                .collect(Collectors.toList());

        if (tagList.isEmpty()) {
            LOGGER.warn("No tags");
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<TagDto>>(tagList) {}).build();
    }
}
