package ar.edu.itba.paw.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//@Controller
//public class ErrorController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
//    @Autowired
//    private MessageSource messageSource;
//
//    @RequestMapping("/403")
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ModelAndView forbidden() {
//        LOGGER.error("FORBIDDEN");
//        String errorCode = "403";
//        String errorMessage = messageSource.getMessage("exception.forbidden", null, LocaleContextHolder.getLocale());
//        return createErrorModel(errorCode, errorMessage);
//    }
//
//    @RequestMapping("/404")
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView notFound() {
//        LOGGER.error("NOT FOUND");
//        String errorCode = "404";
//        String errorMessage = messageSource.getMessage("exception.notFound", null, LocaleContextHolder.getLocale());
//        return createErrorModel(errorCode, errorMessage);
//    }
//
//    public static ModelAndView createErrorModel(String errorCode, String errorMessage) {
//        final ModelAndView mav = new ModelAndView("error");
//        mav.addObject("code", errorCode);
//        mav.addObject("message", errorMessage);
//        return mav;
//    }
//}
