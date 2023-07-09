package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.TicketsException;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Provider
@Component
public class TicketExceptionMapper implements ExceptionMapper<TicketsException> {
    @Autowired
    private MessageSource messageSource;
    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(TicketsException e) {
        final List<ValidationErrorDto> errors = new ArrayList<>();

        for (Map.Entry<Integer, Integer> error : e.getErrorMap().entrySet()) {
            if (error.getValue() <= 0) {
                errors.add(ValidationErrorDto.fromException(
                        messageSource.getMessage("Max.bookForm.qtyReached", null, request.getLocale()),
                        "bookings[" + error.getKey() + "].qty")
                );
            } else {
                errors.add(ValidationErrorDto.fromException(
                        messageSource.getMessage("Max.bookForm.qty", new Object[]{error.getValue()}, request.getLocale()),
                        "bookings[" + error.getKey() + "].qty")
                );
            }
        }

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<List<ValidationErrorDto>>(errors) {})
                .build();
    }
}


