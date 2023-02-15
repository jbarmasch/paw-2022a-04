package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

//import static javax.ws.rs.core.Response.ResponseBuilder;

@Path("api/users")
@Component
public class UserController {
    @Autowired
    private UserService us;
    @Autowired
    private EventService es;
    @Autowired
    private TicketService ts;
    @Autowired
    private EventBookingService bs;

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

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response createUser(@Valid UserForm form) {
        final User user = us.create(form.getUsername(), form.getPassword(), form.getMail(), Locale.ENGLISH);
        
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        return Response.created(uri).build();
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

    @GET
    @Path("/{id}/booking")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id,
                            @QueryParam("eventId") final long eventId) {
        Optional<BookingDto> bookingDto = bs.getBookingFromUser(id, eventId).map(e -> BookingDto.fromBooking(uriInfo, e));

        if (bookingDto.isPresent()) {
            BookingDto aux = bookingDto.get();
            return Response.ok(aux).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserStats(@PathParam("id") final long id) {
        Optional<UserStatsDto> userStatsDto = us.getUserStats(id).map(u -> UserStatsDto.fromUserStats(uriInfo, u));

        if (userStatsDto.isPresent()) {
            return Response.ok(userStatsDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/test")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response testJwt() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        final User user = us.findByUsername(username).orElse(null);

        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
