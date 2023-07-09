package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.webapp.form.TicketForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("api/tickets")
@Component
public class TicketController {
    @Autowired
    private TicketService ts;
    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @Path("/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response modifyTicket(@PathParam("id") final Long ticketId,
                                 @QueryParam("eventId") final Long eventId,
                                 @Valid final TicketForm form) {
        final Ticket ticket = ts.getTicketById(ticketId).orElse(null);

        ts.updateTicket(ticket, form.getTicketName(), form.getPrice(), form.getQty(),
                form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()), form.getMaxPerUser());

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
