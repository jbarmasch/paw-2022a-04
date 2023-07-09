package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;

public class ValidationErrorDto {
    private String message;
    private String path;

    public static ValidationErrorDto fromException(final String message, final String path) {
        final ValidationErrorDto dto = new ValidationErrorDto();
        dto.message = message;
        dto.path = path;
        return dto;
    }

    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> violation) {
        final ValidationErrorDto dto = new ValidationErrorDto();
        dto.message = violation.getMessage();
        dto.path = violation.getPropertyPath().toString();
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
