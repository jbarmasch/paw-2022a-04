package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(final ConstraintViolationException e) {
        final List<ValidationErrorDto> errors = e.getConstraintViolations()
                .stream()
                .map(ValidationErrorDto::fromValidationException)
                .collect(Collectors.toList());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<List<ValidationErrorDto>>(errors) {})
                .build();
    }
}
