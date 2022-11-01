package ar.edu.itba.paw.webapp.controller;

//import ar.edu.itba.paw.model.*;
//import ar.edu.itba.paw.service.EventService;
//import ar.edu.itba.paw.service.UserService;
//import ar.edu.itba.paw.webapp.form.*;
//import ar.edu.itba.paw.webapp.helper.AuthUtils;
//import ar.edu.itba.paw.webapp.auth.UserManager;
//import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Controller
//public class UserController {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private EventService eventService;
//    @Autowired
//    private UserManager userManager;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
//
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public ModelAndView login(@RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
//        if (!userManager.isAuthenticated()) {
//            ModelAndView mav = new ModelAndView("login");
//            if (error == null) {
//                mav.addObject("error", false);
//                AuthUtils.setReferrer(request, request.getHeader("Referer"));
//            } else {
//                LOGGER.error("Incorrect password or username");
//                mav.addObject("error", true);
//            }
//            return mav;
//        }
//
//        LOGGER.debug("User is already authenticated");
//        return new ModelAndView("redirect:/");
//    }
//
//    @RequestMapping(value = "/register", method = { RequestMethod.GET })
//    public ModelAndView createForm(@ModelAttribute("userForm") final UserForm form) {
//        if (!userManager.isAuthenticated())
//            return new ModelAndView("register");
//
//        LOGGER.debug("User is already authenticated");
//        return new ModelAndView("redirect:/");
//    }
//
//    @RequestMapping(value = "/register", method = { RequestMethod.POST })
//    public ModelAndView create(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors, HttpServletRequest request) {
//        if (errors.hasErrors()) {
//            LOGGER.error("UserForm has errors: {}", errors.getAllErrors().toArray());
//            return createForm(form);
//        }
//
//        LOGGER.debug("Create new user with username {}", form.getUsername());
//        userService.create(form.getUsername(), form.getPassword(), form.getMail(), LocaleContextHolder.getLocale());
//        AuthUtils.requestAuthentication(request, form.getUsername(), form.getPassword());
//        return new ModelAndView("redirect:" + AuthUtils.redirectionAuthenticationSuccess(request));
//    }
//
//    @RequestMapping(value = "/forgot-pass", method = RequestMethod.GET)
//    public ModelAndView forgotPass() {
//        if (!userManager.isAuthenticated())
//            return new ModelAndView("forgotPass");
//
//        LOGGER.debug("User is already authenticated");
//        return new ModelAndView("redirect:/");
//    }
//
//    @RequestMapping(value = "/profile", method = { RequestMethod.GET })
//    public ModelAndView getUser() {
//        return new ModelAndView("redirect:/profile/" + userManager.getUserId());
//    }
//
//    @RequestMapping(value = "/profile/{userId}", method = { RequestMethod.GET })
//    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
//        User user = userService.getUserById(userId).orElse(null);
//        if (user == null) {
//            LOGGER.error("User not found");
//            throw new UserNotFoundException();
//        }
//         if (userManager.isAuthenticated() && userManager.getUserId() != userId && !userManager.isCreator(user)) {
//             return new ModelAndView("redirect:/403");
//         }
//        List<Event> events = eventService.getUserEvents(userId, 1);
//
//        final ModelAndView mav = new ModelAndView("profile");
//        if (userManager.isAuthenticated() && userId == userManager.getUserId()) {
//            UserStats stats = userService.getUserStats(userId).orElse(null);
//            LOGGER.debug("User can see stats");
//            mav.addObject("stats", stats);
//        }
//        mav.addObject("user", user);
//        mav.addObject("events", events.stream().limit(5).collect(Collectors.toList()));
//        mav.addObject("size", events.size());
//        return mav;
//    }
//}

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserList;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

//import static javax.ws.rs.core.Response.ResponseBuilder;

@Path("users")
@Component
public class UserController {
    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page) {
        final UserList res = us.getAllUsers(page);
        final List<UserDto> userList = res.getUserList()
                .stream().map(u -> UserDto.fromUser(uriInfo, u)).collect(Collectors.toList());

        int lastPage = res.getPages();

        if (userList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<UserDto>>(userList) {});

        if (page != 1) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        }
        if (page != lastPage) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");
        }

        response
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build(), "last");

        return response.build();
    }

    @POST
    public Response createUser(@QueryParam("username") final String username,
                               @QueryParam("password") final String password) {
        final User user = us.create(username, password, "izuku@gmail.com", Locale.ENGLISH);
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        Optional<UserDto> userDto = us.getUserById(id).map(u -> UserDto.fromUser(uriInfo, u));

        if (userDto.isPresent()) {
            return Response.ok(userDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @DELETE
//    @Path("/{id}")
//    @Produces(value = { MediaType.APPLICATION_JSON, })
//    public Response deleteById(@PathParam("id") final long id) {
//        us.deleteById(id);
//        return Response.noContent().build();
//    }
}
