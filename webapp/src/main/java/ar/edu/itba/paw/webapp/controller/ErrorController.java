package ar.edu.itba.paw.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping("/403")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView forbidden() {
        LOGGER.error("FORBIDDEN");
        String errorCode = "403";
        return createErrorModel(errorCode, "Forbidden");
    }

    @RequestMapping("/404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound() {
        LOGGER.error("NOT FOUND");
        String errorCode = "404";
        return createErrorModel(errorCode, "Page not found");
    }

    public static ModelAndView createErrorModel(String errorCode, String errorMessage) {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", errorCode);
        mav.addObject("message", errorMessage);
        return mav;
    }
}
