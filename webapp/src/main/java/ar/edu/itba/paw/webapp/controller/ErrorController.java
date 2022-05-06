package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
public class ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping("/403")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView forbidden() {
        LOGGER.error("FORBIDDEN");
        String errorCode = "403";
        return createErrorModel(errorCode);
    }

    @ExceptionHandler({EventNotFoundException.class, UserNotFoundException.class, ImageNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(Exception e) {
        LOGGER.error("NOT FOUND {}", e.getMessage());
        String errorCode = "404";
        return createErrorModel(errorCode);
    }

    @SuppressWarnings("deprecation")
    @ExceptionHandler({MethodConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView constraintViolation(Exception e) {
        LOGGER.error("BAD REQUEST {}", e.getMessage());
        String errorCode = "400";
        return createErrorModel(errorCode);
    }

//    @ExceptionHandler({DataIntegrityViolationException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView integrityViolation(Exception e) {
//        LOGGER.error("DATA INTEGRITY VIOLATION {}", e.getMessage());
//        String errorCode = "500";
//        return createErrorModel(errorCode);
//    }

    private ModelAndView createErrorModel(String errorCode) {
        final ModelAndView mav = new ModelAndView("error");
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken))
            username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        mav.addObject("username", username);
        mav.addObject("message", errorCode);
        return mav;
    }
}
