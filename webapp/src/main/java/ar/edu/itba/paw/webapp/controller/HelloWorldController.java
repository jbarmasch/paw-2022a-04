package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {
    private final UserService us;

    @Autowired
    public HelloWorldController(final UserService us) {
        this.us = us;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld(@RequestParam(name = "userId", defaultValue = "1") final long userId) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam(value = "username", required = true) final String username,
                               @RequestParam(value = "password", required = true) final String password) {
            final User u = us.create(username, password);
            return new ModelAndView("redirect:/?userId=" + u.getId());
    }
}
