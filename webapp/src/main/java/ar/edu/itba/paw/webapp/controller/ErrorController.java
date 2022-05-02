package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.hibernate.validator.method.MethodConstraintViolationException;
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

@ControllerAdvice
public class ErrorController {
    @RequestMapping("/403")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView forbidden() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "403");
        return mav;
    }

    @ExceptionHandler({EventNotFoundException.class, UserNotFoundException.class, ImageNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(Exception e) {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "404");
        return mav;
    }

    @SuppressWarnings("deprecation")
    @ExceptionHandler({MethodConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView constraintViolation(Exception e) {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "400");
        return mav;
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView integrityViolation(Exception e) {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "500");
        return mav;
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken))
            username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("username", username);
    }
}
