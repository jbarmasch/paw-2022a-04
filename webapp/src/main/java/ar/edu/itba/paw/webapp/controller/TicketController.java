package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.DateRangeException;
import ar.edu.itba.paw.exceptions.TicketUnderflowException;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.TagService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.TagDto;
import ar.edu.itba.paw.webapp.dto.TicketDto;
import ar.edu.itba.paw.webapp.form.EventForm;
import ar.edu.itba.paw.webapp.form.TicketForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("api/tickets")
@Component
public class TicketController {
    @Autowired
    private TicketService ts;
    @Autowired
    private EventService es;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);


    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listTickets(@PathParam("id") final long id) {
        final List<Ticket> res = ts.getTickets(id);
        final List<TicketDto> ticketList = res
                .stream()
                .map(e -> TicketDto.fromTicket(uriInfo, e))
                .collect(Collectors.toList());

        if (ticketList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<TicketDto>>(ticketList) {});

        return response.build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response addTicket(@QueryParam("eventId") final Long eventId, @Valid final TicketForm form) {
        Optional<Event> event = es.getEventById(eventId);

        if (!event.isPresent()) {
            return Response.serverError().build();
        }

        try {
            ts.addTicket(event.get(), form.getTicketName(), form.getPrice(), form.getQty(),
                    form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()), form.getMaxPerUser());
        } catch (DateRangeException e) {
            return Response.serverError().build();
        }

        return Response.accepted().build();
    }

    @Path("/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response modifyTicket(@PathParam("id") final Long ticketId,
                                 @QueryParam("eventId") final Long eventId,
                                 @Valid final TicketForm form) {
        final Ticket ticket = ts.getTicketById(ticketId).orElse(null);

        try {
            ts.updateTicket(ticket, form.getTicketName(), form.getPrice(), form.getQty(),
                    form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()), form.getMaxPerUser());
        } catch (TicketUnderflowException e) {
            return Response.serverError().build();
        }

        return Response.accepted().build();
    }

    @Path("/{id}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response deleteTicket(@PathParam("id") final Long ticketId) {
        ts.deleteTicket(ticketId);

        return Response.noContent().build();
    }
}
