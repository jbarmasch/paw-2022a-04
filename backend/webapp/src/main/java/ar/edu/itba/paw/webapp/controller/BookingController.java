package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.CodeService;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.webapp.dto.BookingDto;
import ar.edu.itba.paw.webapp.form.BouncerBookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("api/bookings")
@Component
public class BookingController {
    @Autowired
    private EventBookingService bs;
    @Autowired
    private TicketService ts;
    @Autowired
    private CodeService cs;

    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listBookings(@QueryParam("userId") final long userId,
                                 @QueryParam("page") @DefaultValue("1") final int page) {
        final EventBookingList res = bs.getAllBookingsFromUser(userId, page);

        final List<BookingDto> userList = res
                .getBookingList()
                .stream()
                .map(e -> {
                    BookingDto bookingDto = BookingDto.fromBooking(uriInfo, e);
                    String bookUrl = uriInfo.getBaseUriBuilder().toString() + "/bookings/" + e.getCode();
                    byte[] encodeBase64 = Base64.getEncoder().encode(cs.createQr(bookUrl));
                    String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
                    bookingDto.setImage(base64Encoded);
                    return bookingDto;
                })
                .collect(Collectors.toList());

        int lastPage = res.getPages();

        if (userList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<BookingDto>>(userList) {});

        if (page != 1) {
            response.link(uriInfo.getBaseUriBuilder().queryParam("page", page - 1).build(), "prev");
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
    @Path("/{code}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("code") final String code) {
        Optional<BookingDto> bookingDto = bs.getBooking(code).map(e -> BookingDto.fromBooking(uriInfo, e));

        String bookUrl = uriInfo.getBaseUriBuilder().toString() + "/bookings/" + code;
        byte[] encodeBase64 = Base64.getEncoder().encode(cs.createQr(bookUrl));
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);

        if (bookingDto.isPresent()) {
            BookingDto aux = bookingDto.get();
            aux.setImage(base64Encoded);
            return Response.ok(aux).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/{code}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response confirmBooking(@PathParam("code") final String code, final BouncerBookForm bouncerBookForm) {
        EventBooking eventBooking = bs.getBooking(code).orElse(null);

        if (eventBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (bouncerBookForm.isConfirmed()) {
            bs.confirmBooking(eventBooking);
        } else {
            bs.invalidateBooking(eventBooking);
        }
        return Response.accepted().build();
    }

    @Path("/{code}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response cancelBooking(@PathParam("code") final String code) {
        bs.cancelBooking(code, request.getLocale());

        return Response.noContent().build();
    }
}
