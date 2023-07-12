package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventBookingList;
import ar.edu.itba.paw.service.CodeService;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.webapp.dto.BookingDto;
import ar.edu.itba.paw.webapp.exceptions.BookingNotFoundException;
import ar.edu.itba.paw.webapp.form.BouncerBookForm;
import ar.edu.itba.paw.webapp.helper.PaginationUtils;
import ar.edu.itba.paw.webapp.validations.PATCH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Path("api/bookings")
@Component
public class BookingController {
    @Autowired
    private EventBookingService bs;
    @Autowired
    private CodeService cs;
    @Autowired
    private Environment env;
    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listBookings(@QueryParam("userId") final long userId,
                                 @QueryParam("page") @DefaultValue("1") final int page) {
        final EventBookingList res = bs.getAllBookingsFromUser(userId, page);

        final List<BookingDto> bookingList = res
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

        if (bookingList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<BookingDto>>(bookingList) {});
        PaginationUtils.setResponsePages(response, uriInfo, page, res.getPages());
        return response.build();
    }

    @GET
    @Path("/{code}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("code") final String code) {
        BookingDto bookingDto = bs
                .getBooking(code)
                .map(e -> BookingDto.fromBooking(uriInfo, e))
                .orElseThrow(BookingNotFoundException::new);

        String bookUrl = env.getProperty("baseUrl") + "/bookings/" + code;
        byte[] encodeBase64 = Base64.getEncoder().encode(cs.createQr(bookUrl));
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);

        bookingDto.setImage(base64Encoded);
        return Response.ok(bookingDto).build();
    }

    @Path("/{code}")
    @PATCH
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response confirmBooking(@PathParam("code") final String code, final BouncerBookForm bouncerBookForm) {
        EventBooking eventBooking = bs
                .getBooking(code)
                .orElseThrow(BookingNotFoundException::new);

        if (bouncerBookForm.isConfirmed()) {
            bs.confirmBooking(eventBooking);
        } else {
            bs.invalidateBooking(eventBooking);
        }
        return Response.ok().build();
    }

    @Path("/{code}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response cancelBooking(@PathParam("code") final String code) {
        bs.cancelBooking(code, request.getLocale());

        return Response.noContent().build();
    }
}
