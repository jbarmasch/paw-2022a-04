package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.exceptions.OrganizerNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserStatsNotFoundException;
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
        final UserList res = us.filterBy(username, order, page);
        final List<UserDto> userList = res
                .getUserList()
                .stream()
                .map(e -> UserDto.fromUser(uriInfo, e))
                .collect(Collectors.toList());

        if (userList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<UserDto>>(userList) {});
        PaginationUtils.setResponsePages(response, uriInfo, page, res.getPages());
        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        UserDto userDto = us
                .getUserById(id)
                .map(u -> UserDto.fromUser(uriInfo, u))
                .orElseThrow(OrganizerNotFoundException::new);

        return Response.ok(userDto).build();
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
