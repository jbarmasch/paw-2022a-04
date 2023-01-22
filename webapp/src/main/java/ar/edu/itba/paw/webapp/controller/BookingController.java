package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.CodeService;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.webapp.dto.BookingDto;
import ar.edu.itba.paw.webapp.form.BookingForm;
import ar.edu.itba.paw.webapp.form.RateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
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
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listBookings(@QueryParam("userId") final long userId,
                                 @QueryParam("page") @DefaultValue("1") final int page) {
        final EventBookingList res = bs.getAllBookingsFromUser(userId, page);
        final List<BookingDto> userList = res
                .getBookingList()
                .stream()
                .map(e -> BookingDto.fromBooking(uriInfo, e))
                .collect(Collectors.toList());

        int lastPage = res.getPages();

        if (userList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<BookingDto>>(userList) {
        });

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
    @Path("/{code}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("code") final String code) {
        Optional<BookingDto> bookingDto = bs.getBooking(code).map(e -> BookingDto.fromBooking(uriInfo, e));

//        HttpServletRequest request,
//        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String bookUrl = "http://181.46.186.8:2557" + "/bookings/" + code;
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
    @POST
    public Response rateUser(@PathParam("code") final String code) {
        EventBooking eventBooking = bs.getBooking(code).orElse(null);

        if (eventBooking == null) {
//            LOGGER.error("Event not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        bs.confirmBooking(eventBooking);
        return Response.accepted().build();
    }

}
