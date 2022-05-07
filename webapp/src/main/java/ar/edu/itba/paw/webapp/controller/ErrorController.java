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

    public static ModelAndView createErrorModel(String errorCode) {
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
