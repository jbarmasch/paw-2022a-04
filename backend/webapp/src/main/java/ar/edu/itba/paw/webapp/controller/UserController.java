package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.exceptions.BookingNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserStatsNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.helper.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("api/users")
@Component
public class UserController {
    @Autowired
    private UserService us;
    @Autowired
    private EventBookingService bs;
    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUsers(@QueryParam("page") @DefaultValue("1") final int page) {
        final UserList res = us.getAllUsers(page);
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

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response createUser(@Context HttpServletRequest headers, @Valid UserForm form) {
        final User user = us.create(form.getUsername(), form.getPassword(), form.getMail(), request.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        UserDto userDto = us
                .getUserById(id)
                .map(u -> UserDto.fromUser(uriInfo, u))
                .orElseThrow(UserNotFoundException::new);

        return Response.ok(userDto).build();
    }

    @GET
    @Path("/{id}/booking")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id,
                            @QueryParam("eventId") final long eventId) {
        BookingDto bookingDto = bs
                .getBookingFromUser(id, eventId)
                .map(e -> BookingDto.fromBooking(uriInfo, e))
                .orElseThrow(BookingNotFoundException::new);

        return Response.ok(bookingDto).build();
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserStats(@PathParam("id") final long id) {
        UserStatsDto userStatsDto = us
                .getUserStats(id)
                .map(u -> UserStatsDto.fromUserStats(uriInfo, u))
                .orElseThrow(UserStatsNotFoundException::new);

        return Response.ok(userStatsDto).build();
    }
}
