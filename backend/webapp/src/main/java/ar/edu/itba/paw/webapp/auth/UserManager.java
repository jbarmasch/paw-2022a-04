package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
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

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth != null && !(auth instanceof AnonymousAuthenticationToken))) {
            return null;
        }

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();

        return userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public Long getUserId() {
        return getUser().getId();
    }
}
