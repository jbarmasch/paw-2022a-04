package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.*;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorController.class);

    @ExceptionHandler({EventNotFoundException.class, UserNotFoundException.class, ImageNotFoundException.class,
            StatsNotFoundException.class, TicketNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(Exception e) {
        LOGGER.error("NOT FOUND {}", e.getMessage());
        String errorCode = "404";
        return ErrorController.createErrorModel(errorCode);
    }

    @SuppressWarnings("deprecation")
    @ExceptionHandler(MethodConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView constraintViolation(MethodConstraintViolationException e) {
        MethodConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
        LOGGER.error("BAD REQUEST -- {} {}", violation.getParameterName(), violation.getMessage());
        String errorCode = "400";
        return ErrorController.createErrorModel(errorCode);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView typeMismatch(MethodArgumentTypeMismatchException e) {
        LOGGER.error("BAD REQUEST -- {} must be {}", e.getParameter(), e.getRequiredType().getSimpleName());
        String errorCode = "400";
        return ErrorController.createErrorModel(errorCode);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView maxUploadSize(MaxUploadSizeExceededException e) {
        LOGGER.error("INTERNAL SERVER ERROR -- Max upload size is {}", e.getMaxUploadSize());
        String errorCode = "500";
        return ErrorController.createErrorModel(errorCode);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView integrityViolation(Exception e) {
        LOGGER.error("DATA INTEGRITY VIOLATION {}", e.getMessage());
        String errorCode = "500";
        return ErrorController.createErrorModel(errorCode);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView typeMismatch(Exception e) {
        LOGGER.error("PAGE NOT FOUND");
        String errorCode = "404";
        return ErrorController.createErrorModel(errorCode);
    }
}
