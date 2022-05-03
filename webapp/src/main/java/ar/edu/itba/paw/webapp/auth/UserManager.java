package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserManager {
    @Autowired
    private UserService userService;

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !(auth instanceof AnonymousAuthenticationToken);
    }

    public String getUsername() {
        if (isAuthenticated())
            return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return null;
    }

    public User getUser() {
        if (isAuthenticated()) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            return userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
        }
        return null;
    }

    public Integer getUserId() {
        User user = getUser();
        if (user != null)
            return user.getId();
        return null;
    }
}
