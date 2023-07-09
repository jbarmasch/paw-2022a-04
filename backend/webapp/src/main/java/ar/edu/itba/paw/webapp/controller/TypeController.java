package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.service.TypeService;
import ar.edu.itba.paw.webapp.dto.TypeDto;
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

@Path("api/types")
@Component
public class TypeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeController.class);
    @Autowired
    private TypeService ts;
    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listTypes() {
        final List<Type> res = ts.getAll();
        final List<TypeDto> typeList = res
                .stream()
                .map(e -> TypeDto.fromType(uriInfo, e, request.getLocale().getLanguage()))
                .collect(Collectors.toList());

        if (typeList.isEmpty()) {
            LOGGER.warn("No types");
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<TypeDto>>(typeList) {}).build();
    }
}
