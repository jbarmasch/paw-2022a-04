package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserList;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.TicketBookingDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserStatsDto;
import ar.edu.itba.paw.webapp.exceptions.ImageProcessingErrorException;
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
    @Path("/{id}/ticket-bookings")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getTicketBookings(@PathParam("id") final long id,
                                      @QueryParam("eventId") final long eventId) {
        final List<TicketBookingDto> ticketBookingList = bs
                .getTicketBookingsFromUser(id, eventId)
                .stream()
                .map(e -> TicketBookingDto.fromBooking(uriInfo, e))
                .collect(Collectors.toList());

        if (ticketBookingList.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<TicketBookingDto>>(ticketBookingList) {}).build();
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserStats(@PathParam("id") final long id) {
        UserStatsDto userStatsDto = us
                .getUserStats(id)
                .map(u -> UserStatsDto.fromUserStats(uriInfo, u, request.getLocale().getLanguage()))
                .orElse(null);

        if (userStatsDto != null) {
            return Response.ok(userStatsDto).build();
        } else {
            return Response.noContent().build();
        }

    }
}
