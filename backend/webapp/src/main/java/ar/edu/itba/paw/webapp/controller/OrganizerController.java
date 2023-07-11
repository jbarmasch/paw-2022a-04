package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.UserList;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.OrganizerDto;
import ar.edu.itba.paw.webapp.dto.OrganizerStatsDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exceptions.OrganizerNotFoundException;
import ar.edu.itba.paw.webapp.helper.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("api/organizers")
@Component
public class OrganizerController {
    @Autowired
    private UserService us;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response filterBy(@QueryParam("search") final String username,
                             @QueryParam("order") final Order order,
                             @QueryParam("page") @DefaultValue("1") final int page) {
        final UserList res = us.filterByOrganizers(username, order, page);
        final List<OrganizerDto> organizerList = res
                .getUserList()
                .stream()
                .map(e -> OrganizerDto.fromOrganizer(uriInfo, e))
                .collect(Collectors.toList());

        if (organizerList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<OrganizerDto>>(organizerList) {});
        PaginationUtils.setResponsePages(response, uriInfo, page, res.getPages());
        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        OrganizerDto organizerDto = us
                .getOrganizerById(id)
                .map(u -> OrganizerDto.fromOrganizer(uriInfo, u))
                .orElseThrow(OrganizerNotFoundException::new);

        return Response.ok(organizerDto).build();
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getOrganizerStats(@PathParam("id") final long id) {
        OrganizerStatsDto organizerStatsDto = us
                .getOrganizerStats(id)
                .map(u -> OrganizerStatsDto.fromOrganizerStats(uriInfo, u))
                .orElse(null);

        if (organizerStatsDto != null) {
            return Response.ok(organizerStatsDto).build();
        } else {
            return Response.noContent().build();
        }
    }
}
