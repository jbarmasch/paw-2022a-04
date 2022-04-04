package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.MailService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public UserController(final UserService userService, final MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error");
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", userService.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/create", method = { RequestMethod.POST })
    public ModelAndView create(@RequestParam(value = "username") final String username,
                               @RequestParam(value = "password") final String password) {
            final User u = userService.create(username, password);
            return new ModelAndView("redirect:/?userId=" + u.getId());
    }

    @RequestMapping("/mail")
    public ModelAndView sendMail() {
        mailService.sendMail("santilococo.01@gmail.com", "Test", "Probando mandar un mail");
        return new ModelAndView("redirect:/");
    }
}