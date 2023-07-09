package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.CustomErrorDto;
import org.glassfish.jersey.spi.ExtendedExceptionMapper;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExtendedExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        final CustomErrorDto error = CustomErrorDto.fromException(e.getMessage());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<CustomErrorDto>(error) {})
                .build();
    }

    @Override
    public boolean isMappable(RuntimeException e) {
        return !(e.getClass().isAssignableFrom(javax.ws.rs.NotFoundException.class));
    }
}


