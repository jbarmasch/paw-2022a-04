package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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

        int lastPage = res.getPages();

        if (userList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<UserDto>>(userList) {});

        if (page != 1) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        }
        if (page != lastPage) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");
        }

        response
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build(), "last");

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        Optional<UserDto> userDto = us.getUserById(id).map(u -> UserDto.fromUser(uriInfo, u));

        if (userDto.isPresent()) {
            return Response.ok(userDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getOrganizerStats(@PathParam("id") final long id) {
        Optional<OrganizerStatsDto> organizerStatsDto = us.getOrganizerStats(id).map(u -> OrganizerStatsDto.fromOrganizerStats(uriInfo, u));

        if (organizerStatsDto.isPresent()) {
            return Response.ok(organizerStatsDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
