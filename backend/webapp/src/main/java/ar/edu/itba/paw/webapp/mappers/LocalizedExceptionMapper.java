package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;
import ar.edu.itba.paw.webapp.dto.CustomErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class LocalizedExceptionMapper implements ExceptionMapper<LocalizedRuntimeException> {
    @Autowired
    private MessageSource messageSource;
    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(LocalizedRuntimeException e) {
        final CustomErrorDto error = CustomErrorDto.fromException(
                messageSource.getMessage(e.getMessage(), null, request.getLocale())
        );

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<CustomErrorDto>(error) {})
                .build();
    }
}


